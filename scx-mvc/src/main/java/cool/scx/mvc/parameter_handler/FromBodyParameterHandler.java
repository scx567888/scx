package cool.scx.mvc.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.mvc.ScxMvcParameterHandler;
import cool.scx.mvc.ScxMvcRequestInfo;
import cool.scx.mvc.annotation.FromBody;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;

import static cool.scx.mvc.ScxMvcHelper.*;
import static cool.scx.mvc.ScxMvcRequestInfo.ContentType;
import static cool.scx.util.ObjectUtils.*;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromBodyParameterHandler implements ScxMvcParameterHandler {

    /**
     * a
     *
     * @param name       a
     * @param useAllBody a
     * @param required   a
     * @param javaType   a
     * @param info       a
     * @return a
     * @throws RequiredParamEmptyException a
     * @throws ParamConvertException       a
     */
    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMvcRequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        if (info.contentType() == ContentType.FORM) {
            return fromFormAttributes(name, useAllBody, required, javaType, info);
        } else {
            return fromBody(name, useAllBody, required, javaType, info);
        }
    }

    /**
     * <p>fromBody.</p>
     *
     * @param name       a {@link java.lang.String} object
     * @param useAllBody a boolean
     * @param required   a boolean
     * @param javaType   a {@link com.fasterxml.jackson.databind.JavaType} object
     * @param info       a {@link ScxMvcRequestInfo} object
     * @return a {@link java.lang.Object} object
     * @throws RequiredParamEmptyException if any.
     * @throws ParamConvertException       if any.
     */
    private static Object fromBody(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMvcRequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
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

    /**
     * <p>fromFormAttributes.</p>
     *
     * @param name       a {@link java.lang.String} object
     * @param useAllBody a boolean
     * @param required   a boolean
     * @param javaType   a {@link com.fasterxml.jackson.databind.JavaType} object
     * @param info       a {@link ScxMvcRequestInfo} object
     * @return a {@link java.lang.Object} object
     * @throws RequiredParamEmptyException if any.
     * @throws ParamConvertException       if any.
     */
    private static Object fromFormAttributes(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMvcRequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, info.routingContext().request().formAttributes(), useAllBody, javaType);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = convertValue(tempValue, javaType, Option.IGNORE_JSON_IGNORE);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromBody.class) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMvcRequestInfo info) throws Exception {
        var javaType = constructType(parameter.getParameterizedType());
        var required = false;
        var name = parameter.getName();
        var useAllBody = false;

        var fromBody = parameter.getAnnotation(FromBody.class);
        if (fromBody != null) {
            required = fromBody.required();
            if (StringUtils.notBlank(fromBody.value())) {
                name = fromBody.value();
            }
            useAllBody = fromBody.useAllBody();
        }

        return getValueFromBody(name, useAllBody, required, javaType, info);
    }

}
