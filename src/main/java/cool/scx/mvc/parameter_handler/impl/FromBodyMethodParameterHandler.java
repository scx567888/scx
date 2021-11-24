package cool.scx.mvc.parameter_handler.impl;

import cool.scx.annotation.FromBody;
import cool.scx.mvc.parameter_handler.ParamConvertException;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.mvc.parameter_handler.ScxMappingRoutingContextInfo;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public final class FromBodyMethodParameterHandler implements ScxMappingMethodParameterHandler {

    public static final FromBodyMethodParameterHandler DEFAULT_INSTANCE = new FromBodyMethodParameterHandler();

    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, Type type, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        return routingContext.isJsonBody() ? getValueFromJsonBody(name, useAllBody, required, type, routingContext) : getValueFromFormAttributes(name, useAllBody, required, type, routingContext);
    }

    private static Object getValueFromJsonBody(String name, boolean useAllBody, boolean required, Type javaType, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = routingContext.getJsonBody();
        if (!useAllBody) {
            String[] split = name.split("\\.");
            for (String s : split) {
                if (tempValue != null) {
                    tempValue = tempValue.get(s);
                } else {
                    break;
                }
            }
        }
        if (tempValue == null || tempValue.isNull()) {
            //为空的时候做两个处理 即必填则报错 非必填则返回 null
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }

        try {
            return ObjectUtils.readValue(tempValue, javaType);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
            }
        }
        return null;
    }

    private static Object getValueFromFormAttributes(String name, boolean useAllBody, boolean required, Type javaType, ScxMappingRoutingContextInfo routingContext) throws RequiredParamEmptyException, ParamConvertException {
        var v = useAllBody ? routingContext.formAttributesMap() : routingContext.formAttributesMap().get(name);
        if (v == null) {
            //为空的时候做两个处理 即必填则报错 非必填则返回 null
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }

        try {
            return ObjectUtils.convertValue(v, javaType);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromBody.class) != null;
    }

    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo context) throws Exception {
        var javaType = parameter.getParameterizedType();
        var required = false;
        var name = parameter.getName();
        var useAllBody = false;

        var fromBody = parameter.getAnnotation(FromBody.class);
        if (fromBody != null) {
            required = fromBody.required();
            if (StringUtils.isNotBlank(fromBody.value())) {
                name = fromBody.value();
            }
            useAllBody = fromBody.useAllBody();
        }

        return getValueFromBody(name, useAllBody, required, javaType, context);
    }

}
