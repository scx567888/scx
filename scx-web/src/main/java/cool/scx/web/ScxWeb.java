package cool.scx.web;

import cool.scx.common.exception.ScxExceptionHelper;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.routing.Router;
import cool.scx.http.routing.RoutingContext;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.exception_handler.ExceptionHandler;
import cool.scx.web.exception_handler.LastExceptionHandler;
import cool.scx.web.exception_handler.ScxHttpExceptionHandler;
import cool.scx.web.interceptor.DefaultInterceptor;
import cool.scx.web.interceptor.Interceptor;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.web.parameter_handler.from_body.FromBodyParameterHandlerBuilder;
import cool.scx.web.parameter_handler.from_context.FromContextParameterHandlerBuilder;
import cool.scx.web.parameter_handler.from_path.FromPathParameterHandlerBuilder;
import cool.scx.web.parameter_handler.from_query.FromQueryParameterHandlerBuilder;
import cool.scx.web.parameter_handler.from_upload.FromUploadParameterHandlerBuilder;
import cool.scx.web.parameter_handler.last.LastParameterHandlerBuilder;
import cool.scx.web.return_value_handler.*;
import cool.scx.web.template.ScxTemplateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/// ScxWeb
///
/// @author scx567888
/// @version 0.0.1
public final class ScxWeb {

    /// 路由上下文 THREAD_LOCAL
    private static final InheritableThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();
    private final List<ReturnValueHandler> returnValueHandlers = new ArrayList<>();
    private final LastReturnValueHandler lastReturnValueHandler;
    private final List<ParameterHandlerBuilder> parameterHandlerBuilders = new ArrayList<>();
    private final LastParameterHandlerBuilder lastParameterHandlerBuilder;
    private final ScxTemplateHandler templateHandler;
    private final RouteRegistrar routeRegistrar;
    private final WebSocketRouteRegistrar webSocketRouteRegistrar;
    private final ScxWebOptions options;
    private Interceptor interceptor = new DefaultInterceptor();

    public ScxWeb() {
        this(new ScxWebOptions());
    }

    public ScxWeb(ScxWebOptions options) {
        this.options = options;
        this.templateHandler = new ScxTemplateHandler(options.templateRoot());
        this.routeRegistrar = new RouteRegistrar(this);
        this.webSocketRouteRegistrar = new WebSocketRouteRegistrar(this);
        //初始化默认的返回值处理器
        addReturnValueHandler(new NullReturnValueHandler());
        addReturnValueHandler(new StringReturnValueHandler());
        addReturnValueHandler(new TemplateReturnValueHandler(this.templateHandler));
        addReturnValueHandler(new BaseVoReturnValueHandler());
        this.lastReturnValueHandler = new LastReturnValueHandler();
        //初始化默认的参数处理器
        addParameterHandlerBuilder(new FromContextParameterHandlerBuilder());
        addParameterHandlerBuilder(new FromUploadParameterHandlerBuilder());
        addParameterHandlerBuilder(new FromBodyParameterHandlerBuilder());
        addParameterHandlerBuilder(new FromQueryParameterHandlerBuilder());
        addParameterHandlerBuilder(new FromPathParameterHandlerBuilder());
        this.lastParameterHandlerBuilder = new LastParameterHandlerBuilder();
    }

    /// 获取当前线程的 RoutingContext (只限在 scx mapping 注解的方法及其调用链上)
    ///
    /// @return 当前线程的 RoutingContext
    public static RoutingContext routingContext() {
        return ROUTING_CONTEXT_THREAD_LOCAL.get();
    }

    /// 设置当前线程的 routingContext
    /// 此方法正常之给 scxMappingHandler 调用
    /// 若无特殊需求 不必调用此方法
    ///
    /// @param routingContext 要设置的 routingContext
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

    public ScxWeb registerWebSocketRoutes(Router router, Object... objects) {
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

    public ScxWeb addParameterHandlerBuilder(ParameterHandlerBuilder handlerBuilder) {
        parameterHandlerBuilders.add(handlerBuilder);
        return this;
    }

    public ScxWeb addReturnValueHandler(ReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(returnValueHandler);
        return this;
    }

    public ScxWeb addParameterHandlerBuilder(int index, ParameterHandlerBuilder handlerBuilder) {
        parameterHandlerBuilders.add(index, handlerBuilder);
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

    ReturnValueHandler findReturnValueHandler(Object result) {
        for (var handler : returnValueHandlers) {
            if (handler.canHandle(result)) {
                return handler;
            }
        }
        return lastReturnValueHandler;
    }

    ParameterHandler findParameterHandler(ParameterInfo parameter) {
        for (var handler : parameterHandlerBuilders) {
            var parameterHandler = handler.tryBuild(parameter);
            if (parameterHandler != null) {
                return parameterHandler;
            }
        }
        return lastParameterHandlerBuilder.tryBuild(parameter);
    }

    Object[] buildMethodParameters(ParameterHandler[] parameterHandlers, RoutingContext context) throws Exception {
        var info = (RequestInfo) context.data().computeIfAbsent(context.hashCode() + "", (_) -> new RequestInfo(context, options.cachedMultiPart()));
        var exceptionArrayList = new ArrayList<Exception>();
        var methodParameter = new Object[parameterHandlers.length];
        for (int i = 0; i < methodParameter.length; i = i + 1) {
            var methodParameterHandler = parameterHandlers[i];
            try {
                methodParameter[i] = methodParameterHandler.handle(info);
            } catch (ParamConvertException | RequiredParamEmptyException e) {
                exceptionArrayList.add(e);
            }
        }
        if (!exceptionArrayList.isEmpty()) {
            throw new BadRequestException(exceptionArrayList.stream().map(Throwable::getMessage).collect(Collectors.joining(";" + System.lineSeparator())));
        }
        return methodParameter;
    }

    ParameterHandler[] buildParameterHandlers(ParameterInfo[] parameters) {
        var s = new ParameterHandler[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            s[i] = findParameterHandler(parameters[i]);
        }
        return s;
    }

}
