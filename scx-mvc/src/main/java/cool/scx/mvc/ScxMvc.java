package cool.scx.mvc;

import cool.scx.mvc.exception.BadRequestException;
import cool.scx.mvc.exception.InternalServerErrorException;
import cool.scx.mvc.exception.ScxHttpException;
import cool.scx.mvc.exception_handler.LastExceptionHandler;
import cool.scx.mvc.exception_handler.ScxHttpExceptionHandler;
import cool.scx.mvc.interceptor.DefaultInterceptorImpl;
import cool.scx.mvc.parameter_handler.*;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.mvc.return_value_handler.*;
import cool.scx.mvc.websocket.WebSocketRouter;
import cool.scx.standard.HttpStatusCode;
import cool.scx.util.ScxExceptionHelper;
import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ScxMvc {

    /**
     * 路由上下文 THREAD_LOCAL
     */
    private static final InheritableThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();
    private final List<ScxHttpRouterExceptionHandler> exceptionHandlers = new ArrayList<>();
    private final LastExceptionHandler lastExceptionHandler;
    private final List<ScxMvcReturnValueHandler> returnValueHandlers = new ArrayList<>();
    private final LastReturnValueHandler lastReturnValueHandler;
    private final List<ScxMvcParameterHandler> parameterHandlers = new ArrayList<>();
    private final LastParameterHandler lastParameterHandler;
    private final ScxTemplateHandler templateHandler;
    private ScxMvcInterceptor interceptor = new DefaultInterceptorImpl();

    public ScxMvc() {
        this(new ScxMvcOptions());
    }

    public ScxMvc(ScxMvcOptions options) {
        this.templateHandler = new ScxTemplateHandler(options.templateRoot());
        //初始化默认的异常处理器
        addExceptionHandler(new ScxHttpExceptionHandler(options.useDevelopmentErrorPage()));
        this.lastExceptionHandler = new LastExceptionHandler(options.useDevelopmentErrorPage());
        //初始化默认的返回值处理器
        addReturnValueHandler(new NullReturnValueHandler());
        addReturnValueHandler(new StringReturnValueHandler());
        addReturnValueHandler(new HtmlVoReturnValueHandler(this.templateHandler));
        addReturnValueHandler(new BaseVoReturnValueHandler());
        this.lastReturnValueHandler = new LastReturnValueHandler();
        //初始化默认的参数处理器
        addParameterHandler(new RoutingContextParameterHandler());
        addParameterHandler(new UploadedEntityParameterHandler());
        addParameterHandler(new FromBodyParameterHandler());
        addParameterHandler(new FromQueryParameterHandler());
        addParameterHandler(new FromPathParameterHandler());
        this.lastParameterHandler = new LastParameterHandler();
    }

    /**
     * 获取当前线程的 RoutingContext (只限在 scx mapping 注解的方法及其调用链上)
     *
     * @return 当前线程的 RoutingContext
     */
    public static RoutingContext routingContext() {
        return ROUTING_CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程的 routingContext
     * 此方法正常之给 scxMappingHandler 调用
     * 若无特殊需求 不必调用此方法
     *
     * @param routingContext 要设置的 routingContext
     */
    static void _routingContext(RoutingContext routingContext) {
        ROUTING_CONTEXT_THREAD_LOCAL.set(routingContext);
    }

    /**
     * a
     */
    static void _clearRoutingContext() {
        ROUTING_CONTEXT_THREAD_LOCAL.remove();
    }

    public ScxMvc registerHttpRoutes(Router router, Object... objects) {
        new ScxRouteRegistrar(this, objects).registerRoute(router);
        return this;
    }

    public ScxMvc registerWebSocketRoutes(WebSocketRouter router, Object... objects) {
        new ScxWebSocketRouteRegistrar(objects).registerRoute(router);
        return this;
    }

    public ScxMvc setInterceptor(ScxMvcInterceptor newInterceptor) {
        if (newInterceptor == null) {
            throw new IllegalArgumentException("ScxMvcInterceptor must not be empty !!!");
        }
        this.interceptor = newInterceptor;
        return this;
    }

    public ScxMvc addExceptionHandler(ScxHttpRouterExceptionHandler scxHttpRouterExceptionHandler) {
        exceptionHandlers.add(scxHttpRouterExceptionHandler);
        return this;
    }

    public ScxMvc addParameterHandler(ScxMvcParameterHandler handler) {
        parameterHandlers.add(handler);
        return this;
    }

    public ScxMvc addReturnValueHandler(ScxMvcReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(returnValueHandler);
        return this;
    }

    public ScxMvc addExceptionHandler(int index, ScxHttpRouterExceptionHandler handler) {
        exceptionHandlers.add(index, handler);
        return this;
    }

    public ScxMvc addParameterHandler(int index, ScxMvcParameterHandler handler) {
        parameterHandlers.add(index, handler);
        return this;
    }

    public ScxMvc addReturnValueHandler(int index, ScxMvcReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(index, returnValueHandler);
        return this;
    }

    ScxMvcInterceptor interceptor() {
        return interceptor;
    }

    public ScxTemplateHandler templateHandler() {
        return this.templateHandler;
    }

    public ScxHttpRouterExceptionHandler findExceptionHandler(Throwable throwable) {
        for (var handler : exceptionHandlers) {
            if (handler.canHandle(throwable)) {
                return handler;
            }
        }
        return lastExceptionHandler;
    }

    ScxMvcReturnValueHandler findReturnValueHandler(Object result) {
        for (var handler : returnValueHandlers) {
            if (handler.canHandle(result)) {
                return handler;
            }
        }
        return lastReturnValueHandler;
    }

    ScxMvcParameterHandler findParameterHandler(Parameter parameter) {
        for (var handler : parameterHandlers) {
            if (handler.canHandle(parameter)) {
                return handler;
            }
        }
        return lastParameterHandler;
    }

    Object[] buildMethodParameters(Parameter[] parameters, RoutingContext context) throws Exception {
        var info = new ScxMvcRequestInfo(context);
        var exceptionArrayList = new ArrayList<Exception>();
        var methodParameter = new Object[parameters.length];
        for (int i = 0; i < methodParameter.length; i = i + 1) {
            var methodParameterHandler = findParameterHandler(parameters[i]);
            try {
                methodParameter[i] = methodParameterHandler.handle(parameters[i], info);
            } catch (ParamConvertException | RequiredParamEmptyException e) {
                exceptionArrayList.add(e);
            }
        }
        if (!exceptionArrayList.isEmpty()) {
            throw new BadRequestException(exceptionArrayList.stream().map(Throwable::getMessage).collect(Collectors.joining(";" + System.lineSeparator())));
        }
        return methodParameter;
    }

    public ScxMvc bindErrorHandler(Router vertxRouter) {
        var errorHandler = new ErrorHandler(this);
        for (var s : HttpStatusCode.values()) {
            if (s.statusCode() >= 400) {
                vertxRouter.errorHandler(s.statusCode(), errorHandler);
            }
        }
        return this;
    }

    private record ErrorHandler(ScxMvc scxMvc) implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext routingContext) {
            var cause = ScxExceptionHelper.getRootCause(routingContext.failure());
            var statusCode = routingContext.statusCode();
            if (statusCode == 500) { // vertx 会将所有为声明状态码的异常归类为 500 其中就包括 我们的 ScxHttpException
                scxMvc.findExceptionHandler(cause).handle(cause, routingContext);
            } else {
                //不是 500 的话 根据状态码 我们看一下是否能需要进行一次包装
                var status = HttpStatusCode.of(statusCode);
                var scxHttpException = status != null ? new ScxHttpException(status.statusCode(), status.reasonPhrase(), cause) : new InternalServerErrorException(cause);
                scxMvc.findExceptionHandler(scxHttpException).handle(scxHttpException, routingContext);
            }
        }

    }


}
