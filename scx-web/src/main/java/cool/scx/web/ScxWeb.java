package cool.scx.web;

import cool.scx.common.util.ScxExceptionHelper;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.routing.Router;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.routing.WebSocketRouter;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.exception_handler.ExceptionHandler;
import cool.scx.web.exception_handler.LastExceptionHandler;
import cool.scx.web.exception_handler.ScxHttpExceptionHandler;
import cool.scx.web.interceptor.DefaultInterceptor;
import cool.scx.web.interceptor.Interceptor;
import cool.scx.web.parameter_handler.*;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.web.return_value_handler.*;
import cool.scx.web.template.ScxTemplateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class ScxWeb {

    /**
     * 路由上下文 THREAD_LOCAL
     */
    private static final InheritableThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();
    private final List<ExceptionHandler> exceptionHandlers = new ArrayList<>();
    private final LastExceptionHandler lastExceptionHandler;
    private final List<ReturnValueHandler> returnValueHandlers = new ArrayList<>();
    private final LastReturnValueHandler lastReturnValueHandler;
    private final List<ParameterHandler> parameterHandlers = new ArrayList<>();
    private final LastParameterHandler lastParameterHandler;
    private final ScxTemplateHandler templateHandler;
    private final RouterErrorHandler routerErrorHandler;
    private final RouteRegistrar routeRegistrar;
    private final WebSocketRouteRegistrar webSocketRouteRegistrar;
    private Interceptor interceptor = new DefaultInterceptor();

    public ScxWeb() {
        this(new ScxWebOptions());
    }

    public ScxWeb(ScxWebOptions options) {
        this.templateHandler = new ScxTemplateHandler(options.templateRoot());
        this.routerErrorHandler = new RouterErrorHandler(this);
        this.routeRegistrar = new RouteRegistrar(this);
        this.webSocketRouteRegistrar = new WebSocketRouteRegistrar(this);
        //初始化默认的异常处理器
        addExceptionHandler(new ScxHttpExceptionHandler(options.useDevelopmentErrorPage()));
        this.lastExceptionHandler = new LastExceptionHandler(options.useDevelopmentErrorPage());
        //初始化默认的返回值处理器
        addReturnValueHandler(new NullReturnValueHandler());
        addReturnValueHandler(new StringReturnValueHandler());
        addReturnValueHandler(new TemplateReturnValueHandler(this.templateHandler));
        addReturnValueHandler(new BaseVoReturnValueHandler());
        this.lastReturnValueHandler = new LastReturnValueHandler();
        //初始化默认的参数处理器
        addParameterHandler(new RoutingContextParameterHandler());
        addParameterHandler(new FileUploadParameterHandler());
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

    static void _clearRoutingContext() {
        ROUTING_CONTEXT_THREAD_LOCAL.remove();
    }

    public ScxWeb registerHttpRoutes(Router router, Object... objects) {
        routeRegistrar.registerRoute(router, objects);
        return this;
    }

    public ScxWeb registerWebSocketRoutes(WebSocketRouter router, Object... objects) {
        webSocketRouteRegistrar.registerRoute(router, objects);
        return this;
    }

    public ScxWeb setInterceptor(Interceptor newInterceptor) {
        if (newInterceptor == null) {
            throw new IllegalArgumentException("Interceptor must not be empty !!!");
        }
        this.interceptor = newInterceptor;
        return this;
    }

    public ScxWeb addExceptionHandler(ExceptionHandler exceptionHandler) {
        exceptionHandlers.add(exceptionHandler);
        return this;
    }

    public ScxWeb addParameterHandler(ParameterHandler handler) {
        parameterHandlers.add(handler);
        return this;
    }

    public ScxWeb addReturnValueHandler(ReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(returnValueHandler);
        return this;
    }

    public ScxWeb addExceptionHandler(int index, ExceptionHandler handler) {
        exceptionHandlers.add(index, handler);
        return this;
    }

    public ScxWeb addParameterHandler(int index, ParameterHandler handler) {
        parameterHandlers.add(index, handler);
        return this;
    }

    public ScxWeb addReturnValueHandler(int index, ReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(index, returnValueHandler);
        return this;
    }

    Interceptor interceptor() {
        return interceptor;
    }

    public ScxTemplateHandler templateHandler() {
        return this.templateHandler;
    }

    ExceptionHandler findExceptionHandler(Throwable throwable) {
        for (var handler : exceptionHandlers) {
            if (handler.canHandle(throwable)) {
                return handler;
            }
        }
        return lastExceptionHandler;
    }

    ReturnValueHandler findReturnValueHandler(Object result) {
        for (var handler : returnValueHandlers) {
            if (handler.canHandle(result)) {
                return handler;
            }
        }
        return lastReturnValueHandler;
    }

    ParameterHandler findParameterHandler(ParameterInfo parameter) {
        for (var handler : parameterHandlers) {
            if (handler.canHandle(parameter)) {
                return handler;
            }
        }
        return lastParameterHandler;
    }

    Object[] buildMethodParameters(ParameterInfo[] parameters, RoutingContext context) throws Exception {
        var info = new RequestInfo(context);
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

    public ScxWeb bindErrorHandler(Router vertxRouter) {
        vertxRouter.errorHandler(routerErrorHandler);
        return this;
    }

    private record RouterErrorHandler(ScxWeb scxWeb) implements BiConsumer<Throwable, RoutingContext> {

        @Override
        public void accept(Throwable throwable, RoutingContext routingContext) {
            var cause = ScxExceptionHelper.getRootCause(throwable);
            this.scxWeb.findExceptionHandler(cause).handle(cause, routingContext);
        }

    }

}
