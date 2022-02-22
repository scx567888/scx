package cool.scx.web;

import cool.scx.ScxBeanFactory;
import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxMapping;
import cool.scx.config.ScxEasyConfig;
import cool.scx.mvc.ScxMappingHandler;
import cool.scx.web.exception_handler.impl.*;
import cool.scx.web.handler.ScxBodyHandler;
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
import io.vertx.ext.web.handler.impl.FaviconHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;

public final class ScxRouter {

    private static final Logger logger = LoggerFactory.getLogger(ScxRouter.class);

    private final List<ScxMappingHandler> SCX_MAPPING_HANDLER_LIST = new ArrayList<>();

    private final Router vertxRouter;

    //异常 handler
    private final NotFoundExceptionHandler notFoundExceptionHandler;
    private final NoPermExceptionHandler noPermExceptionHandler;
    private final BadRequestExceptionHandler badRequestExceptionHandler;
    private final UnauthorizedExceptionHandler unauthorizedExceptionHandler;
    private final UnsupportedMediaTypeExceptionHandler unsupportedMediaTypeExceptionHandler;
    private final MethodNotAllowedExceptionHandler methodNotAllowedExceptionHandler;
    private final InternalServerErrorExceptionHandler internalServerErrorExceptionHandler;

    //基本 handler
    private final FaviconHandler faviconHandler;
    private final CorsHandler corsHandler;
    private final ScxBodyHandler scxBodyHandler;

    //基本 handler 对应的 路由
    private final Route faviconHandlerRoute;
    private final Route corsHandlerRoute;
    private final Route scxBodyHandlerRoute;

    public ScxRouter(ScxEasyConfig scxEasyConfig, Vertx vertx, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        //创建对应的错误 handler
        this.notFoundExceptionHandler = new NotFoundExceptionHandler();
        this.noPermExceptionHandler = new NoPermExceptionHandler();
        this.badRequestExceptionHandler = new BadRequestExceptionHandler();
        this.unauthorizedExceptionHandler = new UnauthorizedExceptionHandler();
        this.unsupportedMediaTypeExceptionHandler = new UnsupportedMediaTypeExceptionHandler();
        this.methodNotAllowedExceptionHandler = new MethodNotAllowedExceptionHandler();
        this.internalServerErrorExceptionHandler = new InternalServerErrorExceptionHandler();
        //创建 vertxRouter 用来关联整个项目的路由
        this.vertxRouter = Router.router(vertx)
                .errorHandler(404, notFoundExceptionHandler)
                .errorHandler(403, noPermExceptionHandler)
                .errorHandler(400, badRequestExceptionHandler)
                .errorHandler(401, unauthorizedExceptionHandler)
                .errorHandler(415, unsupportedMediaTypeExceptionHandler)
                .errorHandler(405, methodNotAllowedExceptionHandler)
                .errorHandler(500, internalServerErrorExceptionHandler);
        //设置基本的 handler
        this.faviconHandler = new FaviconHandlerImpl(vertx, Path.of(scxEasyConfig.templateRoot().getPath(), "favicon.ico").toString());
        this.corsHandler = new CorsHandlerImpl(scxEasyConfig.allowedOrigin())
                .allowedHeaders(Set.of(HttpHeaderNames.ACCEPT.toString(), HttpHeaderNames.CONTENT_TYPE.toString()))
                .allowedMethods(Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT))
                .allowCredentials(true);
        this.scxBodyHandler = new ScxBodyHandler();
        //注册路由
        this.faviconHandlerRoute = this.vertxRouter.route().handler(faviconHandler);
        this.corsHandlerRoute = this.vertxRouter.route().handler(corsHandler);
        this.scxBodyHandlerRoute = this.vertxRouter.route().handler(scxBodyHandler);
        registerScxMappingHandler(scxBeanFactory, scxModuleInfos);
        registerStaticServerHandler(this.vertxRouter, scxEasyConfig);
    }

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scxModuleInfos a
     */
    private void registerScxMappingHandler(ScxBeanFactory scxBeanFactory, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        SCX_MAPPING_HANDLER_LIST.clear();
        for (var scxModuleInfo : scxModuleInfos) {
            for (var clazz : scxModuleInfo.scxMappingClassList()) {
                for (var method : clazz.getMethods()) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(ScxMapping.class)) {
                        //现根据 注解 和 方法等创建一个路由
                        var s = new ScxMappingHandler(scxBeanFactory, clazz, method);
                        //此处校验路由是否已经存在
                        var b = checkScxMappingHandlerRouteExists(s);
                        if (!b) {
                            SCX_MAPPING_HANDLER_LIST.add(s);
                        }
                    }
                }
            }
        }
        //此处排序的意义在于将 需要正则表达式匹配的 放在最后 防止匹配错误
        SCX_MAPPING_HANDLER_LIST.stream().sorted(Comparator.comparing(ScxMappingHandler::order))
                .forEachOrdered(c -> {
                    var r = this.vertxRouter.route(c.url);
                    for (var httpMethod : c.httpMethods) {
                        r.method(io.vertx.core.http.HttpMethod.valueOf(httpMethod.name()));
                    }
                    r.handler(c);
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

    public Router vertxRouter() {
        return vertxRouter;
    }

    public NotFoundExceptionHandler notFoundExceptionHandler() {
        return notFoundExceptionHandler;
    }

    public NoPermExceptionHandler noPermExceptionHandler() {
        return noPermExceptionHandler;
    }

    public BadRequestExceptionHandler badRequestExceptionHandler() {
        return badRequestExceptionHandler;
    }

    public UnauthorizedExceptionHandler unauthorizedExceptionHandler() {
        return unauthorizedExceptionHandler;
    }

    public UnsupportedMediaTypeExceptionHandler unsupportedMediaTypeExceptionHandler() {
        return unsupportedMediaTypeExceptionHandler;
    }

    public MethodNotAllowedExceptionHandler methodNotAllowedExceptionHandler() {
        return methodNotAllowedExceptionHandler;
    }

    public InternalServerErrorExceptionHandler internalServerErrorExceptionHandler() {
        return internalServerErrorExceptionHandler;
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

    public Route faviconHandlerRoute() {
        return faviconHandlerRoute;
    }

    public Route corsHandlerRoute() {
        return corsHandlerRoute;
    }

    public Route scxBodyHandlerRoute() {
        return scxBodyHandlerRoute;
    }

}
