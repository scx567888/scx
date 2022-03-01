package cool.scx.http;

import cool.scx.ScxBeanFactory;
import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxMapping;
import cool.scx.config.ScxEasyConfig;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception.impl.InternalServerErrorException;
import cool.scx.http.exception_handler.ScxHttpRouterExceptionHandler;
import cool.scx.http.exception_handler.impl.LastExceptionHandler;
import cool.scx.http.exception_handler.impl.ScxHttpExceptionHandler;
import cool.scx.http.handler.ScxBodyHandler;
import cool.scx.mvc.ScxMappingConfiguration;
import cool.scx.mvc.ScxMappingHandler;
import cool.scx.util.exception.ScxExceptionHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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

/**
 * ScxHttp 路由 内部使用 vertxRouter 进行具体路由的处理
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxHttpRouter {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxHttpRouter.class);

    private final List<ScxMappingHandler> SCX_MAPPING_HANDLER_LIST = new ArrayList<>();

    private final Router vertxRouter;

    //基本 handler
    private final FaviconHandler faviconHandler;
    private final CorsHandler corsHandler;
    private final ScxBodyHandler scxBodyHandler;

    //基本 handler 对应的 路由
    private final Route faviconHandlerRoute;
    private final Route corsHandlerRoute;
    private final Route scxBodyHandlerRoute;

    /**
     * 异常处理器列表
     */
    private final List<ScxHttpRouterExceptionHandler> scxHttpRouterExceptionHandlers = new ArrayList<>();

    /**
     * a
     *
     * @param scxMappingConfiguration a
     * @param scxEasyConfig           a
     * @param vertx                   a
     * @param scxModuleInfos          a
     * @param scxBeanFactory          a
     */
    public ScxHttpRouter(ScxMappingConfiguration scxMappingConfiguration, ScxEasyConfig scxEasyConfig, Vertx vertx, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        //初始化默认的异常处理器
        addExceptionHandler(ScxHttpExceptionHandler.DEFAULT_INSTANCE);
        //创建 vertxRouter 用来管理整个项目的路由
        this.vertxRouter = Router.router(vertx);
        //绑定异常处理器
        bindErrorHandler(this.vertxRouter);
        //设置基本的 handler
        this.faviconHandler = new FaviconHandlerImpl(vertx, Path.of(scxEasyConfig.templateRoot().getPath(), "favicon.ico").toString());
        this.corsHandler = new CorsHandlerImpl(scxEasyConfig.allowedOrigin()).allowedHeaders(Set.of(HttpHeaderNames.ACCEPT.toString(), HttpHeaderNames.CONTENT_TYPE.toString())).allowedMethods(Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT)).allowCredentials(true);
        this.scxBodyHandler = new ScxBodyHandler();
        //注册路由
        this.faviconHandlerRoute = this.vertxRouter.route().handler(faviconHandler);
        this.corsHandlerRoute = this.vertxRouter.route().handler(corsHandler);
        this.scxBodyHandlerRoute = this.vertxRouter.route().handler(scxBodyHandler);
        registerScxMappingHandler(scxBeanFactory, scxMappingConfiguration, scxModuleInfos);
        registerStaticServerHandler(this.vertxRouter, scxEasyConfig);
    }

    /**
     * <p>bindErrorHandler.</p>
     *
     * @param vertxRouter a {@link io.vertx.ext.web.Router} object
     */
    private void bindErrorHandler(Router vertxRouter) {
        var errorHandler = new ErrorHandler(this);
        // 因为 ScxHttpResponseStatus 中所有的错误状态 都处在可以处理的范围内 所以我们 按照 ScxHttpResponseStatus 的值进行批量设置
        for (var s : ScxHttpResponseStatus.values()) {
            vertxRouter.errorHandler(s.statusCode(), errorHandler);
        }
    }

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scxHttpRouterConfiguration s
     * @param scxModuleInfos             a
     * @param scxBeanFactory             a {@link cool.scx.ScxBeanFactory} object
     */
    private void registerScxMappingHandler(ScxBeanFactory scxBeanFactory, ScxMappingConfiguration scxHttpRouterConfiguration, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos) {
        SCX_MAPPING_HANDLER_LIST.clear();
        for (var scxModuleInfo : scxModuleInfos) {
            for (var clazz : scxModuleInfo.scxMappingClassList()) {
                for (var method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(ScxMapping.class)) {
                        //现根据 注解 和 方法等创建一个路由
                        var s = new ScxMappingHandler(clazz, method, scxBeanFactory, scxHttpRouterConfiguration, this);
                        //此处校验路由是否已经存在
                        if (!checkScxMappingHandlerRouteExists(s)) {
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
     * @param vertxRouter   a {@link io.vertx.ext.web.Router} object
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
                    logger.error("检测到重复的路由!!! {} --> \"{}\" , 相关 class 及方法如下 ▼" + System.lineSeparator() + "\t{} : {}" + System.lineSeparator() + "\t{} : {}", h, handler.patternUrl, handler.clazz.getName(), handler.method.getName(), a.clazz.getName(), a.method.getName());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * a
     *
     * @return a
     */
    public Router vertxRouter() {
        return vertxRouter;
    }

    /**
     * a
     *
     * @return a
     */
    public FaviconHandler faviconHandler() {
        return faviconHandler;
    }

    /**
     * a
     *
     * @return a
     */
    public CorsHandler corsHandler() {
        return corsHandler;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxBodyHandler scxBodyHandler() {
        return scxBodyHandler;
    }

    /**
     * a
     *
     * @return a
     */
    public Route faviconHandlerRoute() {
        return faviconHandlerRoute;
    }

    /**
     * a
     *
     * @return a
     */
    public Route corsHandlerRoute() {
        return corsHandlerRoute;
    }

    /**
     * a
     *
     * @return a
     */
    public Route scxBodyHandlerRoute() {
        return scxBodyHandlerRoute;
    }

    /**
     * 添加一个 异常处理器
     *
     * @param scxHttpRouterExceptionHandler s
     * @return s
     */
    public ScxHttpRouter addExceptionHandler(ScxHttpRouterExceptionHandler scxHttpRouterExceptionHandler) {
        scxHttpRouterExceptionHandlers.add(scxHttpRouterExceptionHandler);
        return this;
    }

    /**
     * 添加一个 异常处理器
     *
     * @param index                         索引
     * @param scxHttpRouterExceptionHandler s
     * @return s
     */
    public ScxHttpRouter addExceptionHandler(int index, ScxHttpRouterExceptionHandler scxHttpRouterExceptionHandler) {
        scxHttpRouterExceptionHandlers.add(index, scxHttpRouterExceptionHandler);
        return this;
    }

    /**
     * a
     *
     * @param throwable a
     * @return a
     */
    public ScxHttpRouterExceptionHandler findExceptionHandler(Throwable throwable) {
        for (var handler : scxHttpRouterExceptionHandlers) {
            if (handler.canHandle(throwable)) {
                return handler;
            }
        }
        return LastExceptionHandler.DEFAULT_INSTANCE;
    }

    private record ErrorHandler(ScxHttpRouter scxHttpRouter) implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext routingContext) {
            var routingContextException = ScxExceptionHelper.getRootCause(routingContext.failure());
            var routingContextStatusCode = routingContext.statusCode();
            if (routingContextStatusCode == 500) { // vertx 会将所有为声明状态码的异常归类为 500 其中就包括 我们的 ScxHttpException
                scxHttpRouter.findExceptionHandler(routingContextException).handle(routingContextException, routingContext);
            } else {
                //不是 500 的话 根据状态码 我们看一下是否能需要进行一次包装
                var byStatusCode = ScxHttpResponseStatus.findByStatusCode(routingContextStatusCode);
                var scxHttpException = byStatusCode != null ? new ScxHttpException(byStatusCode.statusCode(), byStatusCode.reasonPhrase(), routingContextException) : new InternalServerErrorException(routingContextException);
                scxHttpRouter.findExceptionHandler(scxHttpException).handle(scxHttpException, routingContext);
            }
        }

    }

}
