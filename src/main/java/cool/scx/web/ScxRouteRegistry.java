package cool.scx.web;

import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxMapping;
import cool.scx.config.ScxEasyConfig;
import cool.scx.mvc.ScxMappingHandler;
import cool.scx.web.handler.ScxBodyHandler;
import cool.scx.web.handler.ScxNotFoundHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.impl.CorsHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;

/**
 * ScxRouter 路由注册器
 * 负责将 常用的路由注册到 ScxRouter中
 */
public final class ScxRouteRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ScxRouteRegistry.class);

    private final List<ScxMappingHandler> SCX_MAPPING_HANDLER_LIST = new ArrayList<>();

    private final FaviconHandler faviconHandler;
    private final CorsHandler corsHandler;
    private final ScxBodyHandler scxBodyHandler;
    private final ScxNotFoundHandler scxNotFoundHandler;
    private Route faviconHandlerRoute;
    private Route corsHandlerRoute;
    private Route scxBodyHandlerRoute;
    private Route scxNotFoundHandlerRoute;

    public ScxRouteRegistry(ScxEasyConfig scxEasyConfig, Vertx vertx) {
        this.faviconHandler = FaviconHandler.create(vertx, Path.of(scxEasyConfig.templateRoot().getPath(), "favicon.ico").toString());
        this.corsHandler = getCorsHandlerInstance(scxEasyConfig);
        this.scxBodyHandler = new ScxBodyHandler();
        this.scxNotFoundHandler = new ScxNotFoundHandler();
    }

    private static CorsHandler getCorsHandlerInstance(ScxEasyConfig scxEasyConfig) {
        Set<HttpMethod> allowedMethods = new LinkedHashSet<>();
        Set<String> allowedHeaders = new LinkedHashSet<>();
        boolean allowCredentials = true;
        allowedHeaders.add(HttpHeaderNames.ACCEPT.toString());
        allowedHeaders.add(HttpHeaderNames.CONTENT_TYPE.toString());

        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        return new CorsHandlerImpl(scxEasyConfig.allowedOrigin())
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods)
                .allowCredentials(allowCredentials);
    }

    /**
     * 注册路由
     *
     * @param vertxRouter    a
     * @param scxEasyConfig  a
     * @param scxModuleInfos a
     */
    public void registerAllRoute(Router vertxRouter, ScxEasyConfig scxEasyConfig, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        this.faviconHandlerRoute = vertxRouter.route().handler(faviconHandler);
        this.corsHandlerRoute = vertxRouter.route().handler(corsHandler);
        this.scxBodyHandlerRoute = vertxRouter.route().handler(scxBodyHandler);
        this.scxNotFoundHandlerRoute = vertxRouter.route().order(Integer.MAX_VALUE).handler(scxNotFoundHandler);
        registerScxMappingHandler(vertxRouter, scxModuleInfos);
        registerStaticServerHandler(vertxRouter, scxEasyConfig);
    }

    private void registerScxMappingHandler(Router vertxRouter, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        ScanScxMappingHandlers(scxModuleInfos);
        //此处排序的意义在于将 需要正则表达式匹配的 放在最后 防止匹配错误
        SCX_MAPPING_HANDLER_LIST.stream().sorted(Comparator.comparing(ScxMappingHandler::order))
                .forEachOrdered(c -> {
                    var r = vertxRouter.route(c.url);
                    for (var httpMethod : c.httpMethods) {
                        r.method(io.vertx.core.http.HttpMethod.valueOf(httpMethod.name()));
                    }
                    r.handler(c::handle);
                });
    }

    /**
     * <p>register.</p>
     *
     * @param vertxRouter   a {@link Router} object
     * @param scxEasyConfig a
     */
    private void registerStaticServerHandler(Router vertxRouter, ScxEasyConfig scxEasyConfig) {
        for (var staticServer : scxEasyConfig.staticServers()) {
            vertxRouter.route(staticServer.location())
                    .handler(StaticHandler.create(FileSystemAccess.ROOT, staticServer.root().getPath())
                            .setFilesReadOnly(false));
        }
    }

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scxModuleInfos a
     */
    private void ScanScxMappingHandlers(List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        SCX_MAPPING_HANDLER_LIST.clear();
        for (var scxModuleInfo : scxModuleInfos) {
            for (var clazz : scxModuleInfo.scxMappingClassList()) {
                for (var method : clazz.getMethods()) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(ScxMapping.class)) {
                        //现根据 注解 和 方法等创建一个路由
                        var s = new ScxMappingHandler(clazz, method);
                        //此处校验路由是否已经存在
                        var b = checkScxMappingHandlerRouteExists(s);
                        if (!b) {
                            SCX_MAPPING_HANDLER_LIST.add(s);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取所有被ScxMapping注解标记的方法的 handler
     *
     * @return 所有 handler
     */
    public List<ScxMappingHandler> getAllScxMappingHandler() {
        return SCX_MAPPING_HANDLER_LIST;
    }

    /**
     * 校验路由是否已经存在
     *
     * @param handler h
     * @return true 为存在 false 为不存在
     */
    private boolean checkScxMappingHandlerRouteExists(ScxMappingHandler handler) {
        for (var a : SCX_MAPPING_HANDLER_LIST) {
            if (!a.patternUrl.equals(handler.patternUrl)) {
                continue;
            }
            for (var h : handler.httpMethods) {
                if (Arrays.stream(a.httpMethods).toList().contains(h)) {
                    logger.error("检测到重复的路由!!! {} --> \"{}\" , 相关 class 及方法如下 ▼" + System.lineSeparator() +
                                    "\t{} : {}" + System.lineSeparator() +
                                    "\t{} : {}",
                            h,
                            handler.patternUrl,
                            handler.clazz.getName(),
                            handler.method.getName(),
                            a.clazz.getName(),
                            a.method.getName());
                    return true;
                }
            }
        }
        return false;
    }

    public FaviconHandler faviconHandler() {
        return faviconHandler;
    }

    public CorsHandler corsHandler() {
        return corsHandler;
    }

    public ScxBodyHandler scxBodyHandler() {
        return scxBodyHandler;
    }

    public ScxNotFoundHandler scxNotFoundHandler() {
        return scxNotFoundHandler;
    }

    public Route faviconHandlerRoute() {
        return faviconHandlerRoute;
    }

    public Route corsHandlerRoute() {
        return corsHandlerRoute;
    }

    public Route scxBodyHandlerRoute() {
        return scxBodyHandlerRoute;
    }

    public Route scxNotFoundHandlerRoute() {
        return scxNotFoundHandlerRoute;
    }

}
