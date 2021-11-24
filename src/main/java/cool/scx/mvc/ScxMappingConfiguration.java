package cool.scx.mvc;

import cool.scx.ScxContext;
import cool.scx.enumeration.ScxFeature;
import cool.scx.exception.BadRequestException;
import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import cool.scx.mvc.exception_handler.impl.LastExceptionHandler;
import cool.scx.mvc.exception_handler.impl.ScxHttpExceptionHandler;
import cool.scx.mvc.interceptor.ScxMappingInterceptor;
import cool.scx.mvc.interceptor.impl.ScxMappingInterceptorImpl;
import cool.scx.mvc.parameter_handler.ParamConvertException;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
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
 * a
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
     * 异常处理器
     */
    private final List<ScxMappingExceptionHandler> scxMappingExceptionHandlers = new ArrayList<>();

    /**
     * 拦截器
     */
    private ScxMappingInterceptor scxMappingInterceptor = new ScxMappingInterceptorImpl();

    public ScxMappingConfiguration() {
        //初始化默认的返回值处理器
        addMethodReturnValueHandler(NullMethodReturnValueHandler.DEFAULT_INSTANCE);
        addMethodReturnValueHandler(StringMethodReturnValueHandler.DEFAULT_INSTANCE);
        addMethodReturnValueHandler(BaseVoMethodReturnValueHandler.DEFAULT_INSTANCE);
        //初始化默认的异常处理器
        addExceptionHandler(ScxHttpExceptionHandler.DEFAULT_INSTANCE);
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

    public ScxMappingConfiguration addExceptionHandler(ScxMappingExceptionHandler scxMappingExceptionHandler) {
        scxMappingExceptionHandlers.add(scxMappingExceptionHandler);
        return this;
    }

    /**
     * a
     *
     * @param returnValueHandler a
     */
    public ScxMappingConfiguration addMethodReturnValueHandler(ScxMappingMethodReturnValueHandler returnValueHandler) {
        scxMappingMethodReturnValueHandlers.add(returnValueHandler);
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
     * @param scxMappingMethodParameterHandler a
     * @return a
     */
    public ScxMappingConfiguration addMethodParameterHandler(ScxMappingMethodParameterHandler scxMappingMethodParameterHandler) {
        scxMappingMethodParameterHandlers.add(scxMappingMethodParameterHandler);
        return this;
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
     * a
     *
     * @param throwable a
     * @return a
     */
    public ScxMappingExceptionHandler findExceptionHandler(Throwable throwable) {
        for (var handler : scxMappingExceptionHandlers) {
            if (handler.canHandle(throwable)) {
                return handler;
            }
        }
        return LastExceptionHandler.DEFAULT_INSTANCE;
    }

    public Object[] buildMethodParameters(Parameter[] parameters, RoutingContext context) throws Exception {
        var errMessageList = new ArrayList<Exception>();
        var methodParameter = new Object[parameters.length];
        for (int i = 0; i < methodParameter.length; i++) {
            var methodParameterHandler = ScxContext.scxMappingConfiguration().findMethodParameterHandler(parameters[i]);
            try {
                methodParameter[i] = methodParameterHandler.handle(parameters[i], context);
            } catch (ParamConvertException | RequiredParamEmptyException e) {
                errMessageList.add(e);
            }
        }
        if (!errMessageList.isEmpty()) {
            //是否使用开发时错误页面
            if (ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)) {
                var errMessageInfo = errMessageList.stream().map(Throwable::getMessage).collect(Collectors.joining("," + System.lineSeparator()));
                throw new BadRequestException(errMessageInfo);
            } else {
                throw new BadRequestException();
            }
        }
        return methodParameter;
    }

}
