package cool.scx.core.http;

import cool.scx.core.Scx;
import cool.scx.core.http.exception.InternalServerErrorException;
import cool.scx.core.http.exception_handler.LastExceptionHandler;
import cool.scx.core.http.exception_handler.ScxHttpExceptionHandler;
import cool.scx.util.ScxExceptionHelper;
import io.vertx.core.Handler;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.core.http.ScxHttpHelper.initBodyHandler;
import static cool.scx.core.http.ScxHttpHelper.initCorsHandler;

/**
 * ScxHttp 路由 内部使用 vertxRouter 进行具体路由的处理
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxHttpRouter {

    //vertx 的路由
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
        this.corsHandler = initCorsHandler(scx.scxOptions().allowedOrigin());
        this.bodyHandler = initBodyHandler(scx.scxEnvironment().getTempPath(BodyHandler.DEFAULT_UPLOADS_DIRECTORY));
        //注册路由
        this.corsHandlerRoute = this.vertxRouter.route().handler(corsHandler);
        this.bodyHandlerRoute = this.vertxRouter.route().handler(bodyHandler);
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
