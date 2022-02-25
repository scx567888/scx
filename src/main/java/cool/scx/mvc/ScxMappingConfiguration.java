package cool.scx.mvc;

import cool.scx.ScxContext;
import cool.scx.enumeration.ScxFeature;
import cool.scx.http.exception.impl.BadRequestException;
import cool.scx.mvc.interceptor.ScxMappingInterceptor;
import cool.scx.mvc.interceptor.impl.ScxMappingInterceptorImpl;
import cool.scx.mvc.parameter_handler.ParamConvertException;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.mvc.parameter_handler.ScxMappingRoutingContextInfo;
import cool.scx.mvc.parameter_handler.impl.*;
import cool.scx.mvc.return_value_handler.ScxMappingMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.impl.BaseVoMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.impl.LastMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.impl.NullMethodReturnValueHandler;
import cool.scx.mvc.return_value_handler.impl.StringMethodReturnValueHandler;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ScxMappingConfiguration 配置类 再此处可配置 [前置后置拦截器,参数处理器,返回值处理器等]
 */
public final class ScxMappingConfiguration {

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

    /**
     * a
     */
    public ScxMappingConfiguration() {
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
    public ScxMappingConfiguration setScxMappingInterceptor(ScxMappingInterceptor scxMappingInterceptor) {
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
    public ScxMappingConfiguration addMethodParameterHandler(ScxMappingMethodParameterHandler scxMappingMethodParameterHandler) {
        scxMappingMethodParameterHandlers.add(scxMappingMethodParameterHandler);
        return this;
    }

    /**
     * 添加一个方法返回值处理器
     *
     * @param returnValueHandler a
     * @return a
     */
    public ScxMappingConfiguration addMethodReturnValueHandler(ScxMappingMethodReturnValueHandler returnValueHandler) {
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
    public ScxMappingConfiguration addMethodParameterHandler(int index, ScxMappingMethodParameterHandler scxMappingMethodParameterHandler) {
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
    public ScxMappingConfiguration addMethodReturnValueHandler(int index, ScxMappingMethodReturnValueHandler returnValueHandler) {
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
     * @throws Exception r
     */
    public Object[] buildMethodParameters(Parameter[] parameters, RoutingContext context) throws Exception {
        var scxMappingRoutingContextInfo = new ScxMappingRoutingContextInfo(context);
        var errMessageList = new ArrayList<Exception>();
        var methodParameter = new Object[parameters.length];
        for (int i = 0; i < methodParameter.length; i++) {
            var methodParameterHandler = findMethodParameterHandler(parameters[i]);
            try {
                methodParameter[i] = methodParameterHandler.handle(parameters[i], scxMappingRoutingContextInfo);
            } catch (ParamConvertException | RequiredParamEmptyException e) {
                errMessageList.add(e);
            }
        }
        if (!errMessageList.isEmpty()) {
            //是否使用开发时错误页面
            if (ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)) {
                throw new BadRequestException(errMessageList.stream().map(Throwable::getMessage).collect(Collectors.joining(";" + System.lineSeparator())));
            } else {
                throw new BadRequestException();
            }
        }
        return methodParameter;
    }

}
