package cool.scx.core.http;

import cool.scx.core.Scx;
import cool.scx.core.ScxConstant;
import cool.scx.core.annotation.ScxMapping;
import cool.scx.core.http.exception.ScxHttpException;
import cool.scx.core.http.exception.impl.InternalServerErrorException;
import cool.scx.core.http.exception_handler.ScxHttpRouterExceptionHandler;
import cool.scx.core.http.exception_handler.impl.LastExceptionHandler;
import cool.scx.core.http.exception_handler.impl.ScxHttpExceptionHandler;
import cool.scx.core.mvc.ScxMappingHandler;
import cool.scx.util.ScxExceptionHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.impl.CorsHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final CorsHandler corsHandler;
    private final BodyHandler bodyHandler;

    //基本 handler 对应的 路由
    private final Route corsHandlerRoute;
    private final Route bodyHandlerRoute;

    /**
     * 异常处理器列表
     */
    private final List<ScxHttpRouterExceptionHandler> scxHttpRouterExceptionHandlers = new ArrayList<>();

    /**
     * a
     *
     * @param scx a
     */
    public ScxHttpRouter(Scx scx) {
        //初始化默认的异常处理器
        addExceptionHandler(ScxHttpExceptionHandler.DEFAULT_INSTANCE);
        //创建 vertxRouter 用来管理整个项目的路由
        this.vertxRouter = Router.router(scx.vertx());
        //绑定异常处理器
        bindErrorHandler(this.vertxRouter);
        //设置基本的 handler
        this.corsHandler = new CorsHandlerImpl(scx.scxCoreConfig().allowedOrigin()).allowedHeaders(Set.of(HttpHeaderNames.ACCEPT.toString(), HttpHeaderNames.CONTENT_TYPE.toString())).allowedMethods(Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT)).allowCredentials(true);
        this.bodyHandler = BodyHandler.create(scx.scxEnvironment().getTempPath(BodyHandler.DEFAULT_UPLOADS_DIRECTORY).toString()).setBodyLimit(ScxConstant.DEFAULT_BODY_LIMIT).setMergeFormAttributes(false).setDeleteUploadedFilesOnEnd(true);
        //注册路由
        this.corsHandlerRoute = this.vertxRouter.route().handler(corsHandler);
        this.bodyHandlerRoute = this.vertxRouter.route().handler(bodyHandler);
        registerScxMappingHandler(scx);
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
     * @param scx s
     */
    private void registerScxMappingHandler(Scx scx) {
        var metadataList = scx.scxModules();
        SCX_MAPPING_HANDLER_LIST.clear();
        for (var m : metadataList) {
            for (var clazz : m.scxMappingClassList()) {
                for (var method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(ScxMapping.class)) {
                        //现根据 注解 和 方法等创建一个路由
                        var s = new ScxMappingHandler(clazz, method, scx, this);
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
    public CorsHandler corsHandler() {
        return corsHandler;
    }

    /**
     * a
     *
     * @return a
     */
    public BodyHandler bodyHandler() {
        return bodyHandler;
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
    public Route bodyHandlerRoute() {
        return bodyHandlerRoute;
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
