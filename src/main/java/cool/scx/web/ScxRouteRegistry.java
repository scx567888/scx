package cool.scx.web;

import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxMapping;
import cool.scx.config.ScxEasyConfig;
import cool.scx.mvc.ScxMappingHandler;
import cool.scx.web.handler.ScxBodyHandler;
import cool.scx.web.handler.ScxCookieHandlerConfiguration;
import cool.scx.web.handler.ScxCorsHandlerConfiguration;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.impl.CorsHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * ScxRouter 路由注册器
 * 负责将 常用的路由注册到 ScxRouter中
 */
public final class ScxRouteRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ScxRouteRegistry.class);

    private static final List<ScxMappingHandler> SCX_MAPPING_HANDLER_LIST = new ArrayList<>();

    /**
     * 注册路由
     *
     * @param vertxRouter a
     */
    public static void registerAllRoute(Router vertxRouter, ScxEasyConfig scxEasyConfig, Vertx vertx, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        registerFaviconHandler(vertxRouter, scxEasyConfig, vertx);
        registerCookieHandler(vertxRouter);
        registerCorsHandler(vertxRouter, scxEasyConfig);
        registerBodyHandler(vertxRouter);
        registerScxMappingHandler(vertxRouter, scxModuleInfos);
        registerStaticServerHandler(vertxRouter, scxEasyConfig);
        registerNotFoundHandler(vertxRouter);
    }

    /**
     * <p>register.</p>
     *
     * @param vertxRouter   a {@link Router} object
     * @param scxEasyConfig a
     * @param vertx         a
     */
    private static void registerFaviconHandler(Router vertxRouter, ScxEasyConfig scxEasyConfig, Vertx vertx) {
        var faviconHandlerRoute = FaviconHandler.create(vertx, Path.of(scxEasyConfig.templateRoot().getPath(), "favicon.ico").toString());
        vertxRouter.route().handler(faviconHandlerRoute);
    }

    /**
     * <p>register.</p>
     *
     * @param vertxRouter a {@link io.vertx.ext.web.Router} object
     */
    private static void registerCookieHandler(Router vertxRouter) {
        var cookieHandlerRoute = ScxCookieHandlerConfiguration.getScxCookieHandler();
        vertxRouter.route().handler(cookieHandlerRoute);
    }

    private static void registerCorsHandler(Router vertxRouter, ScxEasyConfig scxEasyConfig) {
        var corsHandlerRoute = new CorsHandlerImpl(scxEasyConfig.allowedOrigin())
                .allowedHeaders(ScxCorsHandlerConfiguration.allowedHeaders())
                .allowedMethods(ScxCorsHandlerConfiguration.allowedMethods())
                .allowCredentials(ScxCorsHandlerConfiguration.isAllowCredentials());
        vertxRouter.route().handler(corsHandlerRoute);
    }

    private static void registerBodyHandler(Router vertxRouter) {
        vertxRouter.route().handler(new ScxBodyHandler());
    }

    private static void registerScxMappingHandler(Router vertxRouter, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
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
    private static void registerStaticServerHandler(Router vertxRouter, ScxEasyConfig scxEasyConfig) {
        for (var staticServer : scxEasyConfig.staticServers()) {
            vertxRouter.route(staticServer.location())
                    .handler(StaticHandler.create()
                            .setAllowRootFileSystemAccess(true)
                            .setWebRoot(staticServer.root().getPath()));
        }
    }

    private static void registerNotFoundHandler(Router vertxRouter) {
        var notFoundHandler = (Handler<RoutingContext>) ctx -> ctx.response().setStatusCode(404).end("Not Found !!!");
        vertxRouter.route().handler(notFoundHandler);
    }

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scxModuleInfos a
     */
    private static void ScanScxMappingHandlers(List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
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
    public static List<ScxMappingHandler> getAllScxMappingHandler() {
        return SCX_MAPPING_HANDLER_LIST;
    }

    /**
     * 校验路由是否已经存在
     *
     * @param handler h
     * @return true 为存在 false 为不存在
     */
    private static boolean checkScxMappingHandlerRouteExists(ScxMappingHandler handler) {
        for (var a : SCX_MAPPING_HANDLER_LIST) {
            if (!a.patternUrl.equals(handler.patternUrl)) {
                continue;
            }
            for (var h : handler.httpMethods) {
                if (Arrays.stream(a.httpMethods).toList().contains(h)) {
                    logger.error("检测到重复的路由!!! {} --> \"{}\" , 相关 class 及方法如下 ▼" + System.lineSeparator() +
                                    "{} --> {}" + System.lineSeparator() +
                                    "{} --> {}",
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


}
