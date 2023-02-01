package cool.scx.mvc;

import cool.scx.mvc.Interceptor.ScxMappingInterceptorImpl;
import cool.scx.mvc.exception.BadRequestException;
import cool.scx.mvc.exception.InternalServerErrorException;
import cool.scx.mvc.exception_handler.LastExceptionHandler;
import cool.scx.mvc.exception_handler.ScxHttpExceptionHandler;
import cool.scx.mvc.parameter_handler.*;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.mvc.return_value_handler.*;
import cool.scx.mvc.websocket.ScxWebSocketRouter;
import cool.scx.util.ScxExceptionHelper;
import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ScxMvc {

    private final List<ScxHttpRouterExceptionHandler> exceptionHandlers = new ArrayList<>();

    private final LastExceptionHandler lastExceptionHandler;

    private final List<ScxMvcReturnValueHandler> returnValueHandlers = new ArrayList<>();

    private final LastReturnValueHandler lastReturnValueHandler;

    private final List<ScxMvcParameterHandler> parameterHandlers = new ArrayList<>();

    private final LastParameterHandler lastParameterHandler;

    private final ScxMvcOptions options;

    private ScxMvcInterceptor interceptor = new ScxMappingInterceptorImpl();

    public ScxMvc() {
        this(new ScxMvcOptions());
    }

    public ScxMvc(ScxMvcOptions options) {
        this.options = options;
        //初始化默认的异常处理器
        addExceptionHandler(new ScxHttpExceptionHandler(options.useDevelopmentErrorPage()));
        this.lastExceptionHandler = new LastExceptionHandler(options.useDevelopmentErrorPage());
        //初始化默认的返回值处理器
        addReturnValueHandler(new NullReturnValueHandler());
        addReturnValueHandler(new StringReturnValueHandler());
        addReturnValueHandler(new HtmlVoReturnValueHandler(options.templateRoot()));
        addReturnValueHandler(new BaseVoReturnValueHandler());
        this.lastReturnValueHandler = new LastReturnValueHandler();
        //初始化默认的参数处理器
        addParameterHandler(new RoutingContextMethodParameterHandler());
        addParameterHandler(new UploadedEntityMethodParameterHandler());
        addParameterHandler(new FromBodyMethodParameterHandler());
        addParameterHandler(new FromQueryMethodParameterHandler());
        addParameterHandler(new FromPathMethodParameterHandler());
        this.lastParameterHandler = new LastParameterHandler();
    }

    public ScxMvc registerHttpRoutes(Router router, BeanFactory beanFactory, List<Class<?>> classList) {
        new ScxMappingRegistrar(this, beanFactory, classList).registerRoute(router);
        return this;
    }

    public ScxMvc registerWebSocketRoutes(ScxWebSocketRouter router, BeanFactory beanFactory, List<Class<?>> classList) {
        new ScxWebSocketMappingRegistrar(beanFactory, classList).registerRoute(router);
        return this;
    }

    public ScxMvc setInterceptor(ScxMvcInterceptor scxMappingInterceptor) {
        if (scxMappingInterceptor == null) {
            throw new IllegalArgumentException("ScxMappingInterceptor must not be empty !!!");
        }
        this.interceptor = scxMappingInterceptor;
        return this;
    }

    public ScxMvc addExceptionHandler(ScxHttpRouterExceptionHandler scxHttpRouterExceptionHandler) {
        exceptionHandlers.add(scxHttpRouterExceptionHandler);
        return this;
    }

    public ScxMvc addParameterHandler(ScxMvcParameterHandler scxMappingMethodParameterHandler) {
        parameterHandlers.add(scxMappingMethodParameterHandler);
        return this;
    }

    public ScxMvc addReturnValueHandler(ScxMvcReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(returnValueHandler);
        return this;
    }

    public ScxMvc addExceptionHandler(int index, ScxHttpRouterExceptionHandler scxHttpRouterExceptionHandler) {
        exceptionHandlers.add(index, scxHttpRouterExceptionHandler);
        return this;
    }

    public ScxMvc addParameterHandler(int index, ScxMvcParameterHandler scxMappingMethodParameterHandler) {
        parameterHandlers.add(index, scxMappingMethodParameterHandler);
        return this;
    }

    public ScxMvc addReturnValueHandler(int index, ScxMvcReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(index, returnValueHandler);
        return this;
    }

    ScxMvcInterceptor interceptor() {
        return interceptor;
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
        var info = new ScxMappingRoutingContextInfo(context);
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
        // 因为 ScxHttpResponseStatus 中所有的错误状态 都处在可以处理的范围内 所以我们 按照 ScxHttpResponseStatus 的值进行批量设置
        for (var s : ScxHttpResponseStatus.values()) {
            vertxRouter.errorHandler(s.statusCode(), errorHandler);
        }
        return this;
    }

    private record ErrorHandler(ScxMvc scxMvc) implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext routingContext) {
            var routingContextException = ScxExceptionHelper.getRootCause(routingContext.failure());
            var routingContextStatusCode = routingContext.statusCode();
            if (routingContextStatusCode == 500) { // vertx 会将所有为声明状态码的异常归类为 500 其中就包括 我们的 ScxHttpException
                scxMvc.findExceptionHandler(routingContextException).handle(routingContextException, routingContext);
            } else {
                //不是 500 的话 根据状态码 我们看一下是否能需要进行一次包装
                var byStatusCode = ScxHttpResponseStatus.findByStatusCode(routingContextStatusCode);
                var scxHttpException = byStatusCode != null ? new ScxHttpException(byStatusCode.statusCode(), byStatusCode.reasonPhrase(), routingContextException) : new InternalServerErrorException(routingContextException);
                scxMvc.findExceptionHandler(scxHttpException).handle(scxHttpException, routingContext);
            }
        }

    }


}
