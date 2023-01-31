package cool.scx.mvc;

import cool.scx.beans.ScxBeanFactory;
import cool.scx.mvc.Interceptor.ScxMappingInterceptorImpl;
import cool.scx.mvc.http.ScxHttpRouter;
import cool.scx.mvc.http.exception.BadRequestException;
import cool.scx.mvc.parameter_handler.*;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.mvc.registrar.ScxMappingRegistrar;
import cool.scx.mvc.registrar.ScxWebSocketMappingRegistrar;
import cool.scx.mvc.return_value_handler.BaseVoMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.LastMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.NullMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.StringMethodReturnValueHandler;
import cool.scx.mvc.websocket.ScxWebSocketRouter;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScxMvc {

    private final ScxMvcOptions options;
    private final Vertx vertx;
    private final ScxBeanFactory beanFactory;
    private final ScxHttpRouter scxHttpRouter;
    private final ScxWebSocketRouter scxWebSocketRouter;
    /**
     * 方法返回值处理器
     */
    private final List<ScxMappingMethodReturnValueHandler> scxMappingMethodReturnValueHandlers = new ArrayList<>();
    /**
     * 方法参数处理器
     */
    private final List<ScxMappingMethodParameterHandler> scxMappingMethodParameterHandlers = new ArrayList<>();
    /**
     * 拦截器
     */
    private ScxMappingInterceptor scxMappingInterceptor = new ScxMappingInterceptorImpl();

    public ScxMvc(ScxMvcOptions options, Vertx vertx, ScxBeanFactory beanFactory) {
        this.options = options;
        this.vertx = vertx;
        this.beanFactory = beanFactory;
        init();
        this.scxHttpRouter = new ScxHttpRouter(this);
        this.scxWebSocketRouter = new ScxWebSocketRouter();
        //3, 注册 ScxMapping 和 ScxWebSocketMapping 注解的 handler 到 路由中去
        new ScxMappingRegistrar(this, options.classList()).registerRoute(this.scxHttpRouter.vertxRouter());
        new ScxWebSocketMappingRegistrar(this, options.classList()).registerRoute(this.scxWebSocketRouter);

    }

    public ScxWebSocketRouter scxWebSocketRouter() {
        return scxWebSocketRouter;
    }

    public Vertx vertx() {
        return vertx;
    }

    public ScxMvcOptions options() {
        return options;
    }

    public ScxBeanFactory beanFactory() {
        return beanFactory;
    }

    public ScxTemplate template() {
        return null;
    }

    public ScxHttpRouter scxHttpRouter() {
        return scxHttpRouter;
    }

    /**
     * a
     */
    public void init() {
        //初始化默认的返回值处理器
        addMethodReturnValueHandler(NullMethodReturnValueHandler.DEFAULT_INSTANCE);
        addMethodReturnValueHandler(StringMethodReturnValueHandler.DEFAULT_INSTANCE);
        addMethodReturnValueHandler(BaseVoMethodReturnValueHandler.DEFAULT_INSTANCE);
        //初始化默认的参数处理器
        addMethodParameterHandler(RoutingContextMethodParameterHandler.DEFAULT_INSTANCE);
        addMethodParameterHandler(UploadedEntityMethodParameterHandler.DEFAULT_INSTANCE);
        addMethodParameterHandler(FromBodyMethodParameterHandler.DEFAULT_INSTANCE);
        addMethodParameterHandler(FromQueryMethodParameterHandler.DEFAULT_INSTANCE);
        addMethodParameterHandler(FromPathMethodParameterHandler.DEFAULT_INSTANCE);
    }

    /**
     * a
     *
     * @param scxMappingInterceptor a
     * @return a
     */
    public ScxMvc setScxMappingInterceptor(ScxMappingInterceptor scxMappingInterceptor) {
        if (scxMappingInterceptor == null) {
            throw new IllegalArgumentException("ScxMappingInterceptor must not be empty !!!");
        }
        this.scxMappingInterceptor = scxMappingInterceptor;
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxMappingInterceptor scxMappingInterceptor() {
        return scxMappingInterceptor;
    }

    /**
     * 添加一个 方法参数处理器
     *
     * @param scxMappingMethodParameterHandler a
     * @return a
     */
    public ScxMvc addMethodParameterHandler(ScxMappingMethodParameterHandler scxMappingMethodParameterHandler) {
        scxMappingMethodParameterHandlers.add(scxMappingMethodParameterHandler);
        return this;
    }

    /**
     * 添加一个方法返回值处理器
     *
     * @param returnValueHandler a
     * @return a
     */
    public ScxMvc addMethodReturnValueHandler(ScxMappingMethodReturnValueHandler returnValueHandler) {
        scxMappingMethodReturnValueHandlers.add(returnValueHandler);
        return this;
    }

    /**
     * 添加一个 方法参数处理器
     *
     * @param index                            索引
     * @param scxMappingMethodParameterHandler a
     * @return a
     */
    public ScxMvc addMethodParameterHandler(int index, ScxMappingMethodParameterHandler scxMappingMethodParameterHandler) {
        scxMappingMethodParameterHandlers.add(index, scxMappingMethodParameterHandler);
        return this;
    }

    /**
     * 添加一个方法返回值处理器
     *
     * @param index              索引
     * @param returnValueHandler a
     * @return a
     */
    public ScxMvc addMethodReturnValueHandler(int index, ScxMappingMethodReturnValueHandler returnValueHandler) {
        scxMappingMethodReturnValueHandlers.add(index, returnValueHandler);
        return this;
    }

    /**
     * a
     *
     * @param result a
     * @return a
     */
    public ScxMappingMethodReturnValueHandler findMethodReturnValueHandler(Object result) {
        for (var handler : scxMappingMethodReturnValueHandlers) {
            if (handler.canHandle(result)) {
                return handler;
            }
        }
        return LastMethodReturnValueHandler.DEFAULT_INSTANCE;
    }

    /**
     * a
     *
     * @param parameter a
     * @return a
     */
    public ScxMappingMethodParameterHandler findMethodParameterHandler(Parameter parameter) {
        for (var handler : scxMappingMethodParameterHandlers) {
            if (handler.canHandle(parameter)) {
                return handler;
            }
        }
        return LastMethodParameterHandler.DEFAULT_INSTANCE;
    }

    /**
     * 构建方法参数
     *
     * @param parameters p
     * @param context    c
     * @return r
     * @throws java.lang.Exception r
     */
    public Object[] buildMethodParameters(Parameter[] parameters, RoutingContext context) throws Exception {
        var info = new ScxMappingRoutingContextInfo(context);
        var exceptionArrayList = new ArrayList<Exception>();
        var methodParameter = new Object[parameters.length];
        for (int i = 0; i < methodParameter.length; i = i + 1) {
            var methodParameterHandler = findMethodParameterHandler(parameters[i]);
            try {
                methodParameter[i] = methodParameterHandler.handle(parameters[i], info);
            } catch (ParamConvertException | RequiredParamEmptyException e) {
                exceptionArrayList.add(e);
            }
        }
        if (!exceptionArrayList.isEmpty()) {
            //是否使用开发时错误页面
            throw new BadRequestException(exceptionArrayList.stream().map(Throwable::getMessage).collect(Collectors.joining(";" + System.lineSeparator())));
        }
        return methodParameter;
    }

}
