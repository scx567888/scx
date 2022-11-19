package cool.scx.core.mvc.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.core.annotation.FromBody;
import cool.scx.core.mvc.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.ScxMappingRequestInfo;
import cool.scx.core.mvc.exception.ParamConvertException;
import cool.scx.core.mvc.exception.RequiredParamEmptyException;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;

import static cool.scx.core.mvc.ScxMappingHelper.*;
import static cool.scx.util.ObjectUtils.*;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromBodyMethodParameterHandler implements ScxMappingMethodParameterHandler {

    /**
     * a
     */
    public static final FromBodyMethodParameterHandler DEFAULT_INSTANCE = new FromBodyMethodParameterHandler();

    /**
     * a
     *
     * @param name        a
     * @param useAllBody  a
     * @param required    a
     * @param javaType    a
     * @param requestInfo a
     * @return a
     * @throws cool.scx.core.mvc.exception.RequiredParamEmptyException a
     * @throws cool.scx.core.mvc.exception.ParamConvertException       a
     */
    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMappingRequestInfo requestInfo) throws RequiredParamEmptyException, ParamConvertException {
        if (requestInfo.contentType() == ScxMappingRequestInfo.ContentType.FORM) {
            return fromFormAttributes(name, useAllBody, required, javaType, requestInfo);
        } else {
            return fromBody(name, useAllBody, required, javaType, requestInfo);
        }
    }

    private static Object fromBody(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMappingRequestInfo requestInfo) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromJsonNode(name, requestInfo.body(), useAllBody);
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

    private static Object fromFormAttributes(String name, boolean useAllBody, boolean required, JavaType javaType, ScxMappingRequestInfo requestInfo) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, requestInfo.routingContext().request().formAttributes(), useAllBody, javaType);
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
    public Object handle(Parameter parameter, ScxMappingRequestInfo requestInfo) throws Exception {
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

        return getValueFromBody(name, useAllBody, required, javaType, requestInfo);
    }

}
