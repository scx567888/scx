package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.common.reflect.ParameterInfo;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.common.util.ObjectUtils.Options;
import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.web.ScxWebHelper.*;
import static cool.scx.web.parameter_handler.RequestInfo.ContentType;

/**
 * FromBodyParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromBodyParameterHandler implements ParameterHandler {

    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        if (info.contentType() == ContentType.FORM) {
            return fromFormAttributes(name, useAllBody, required, javaType, info);
        } else {
            return fromBody(name, useAllBody, required, javaType, info);
        }
    }

    private static Object fromBody(String name, boolean useAllBody, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromJsonNode(name, info.body(), useAllBody);
        // 为了提高性能这里提前做一次校验
        if (tempValue == null || tempValue.isNull() || tempValue.isMissingNode()) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = readValue(tempValue, javaType);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    private static Object fromFormAttributes(String name, boolean useAllBody, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, info.routingContext().request().formAttributes(), useAllBody, javaType);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = convertValue(tempValue, javaType, new Options().setIgnoreJsonIgnore(true));
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.parameter().getAnnotation(FromBody.class) != null;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var fromBody = parameter.parameter().getAnnotation(FromBody.class);
        if (fromBody == null) {
            throw new IllegalArgumentException("参数没有 FromBody 注解 !!!");
        }
        var value = getAnnotationValue(fromBody.value());
        return getValueFromBody(value != null ? value : parameter.name(), fromBody.useAllBody(), fromBody.required(), parameter.type(), requestInfo);
    }

}
