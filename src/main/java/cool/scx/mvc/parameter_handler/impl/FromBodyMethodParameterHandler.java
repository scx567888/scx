package cool.scx.mvc.parameter_handler.impl;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.annotation.FromBody;
import cool.scx.mvc.parameter_handler.ParamConvertException;
import cool.scx.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.mvc.parameter_handler.ScxMappingRoutingContextInfo;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

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
     * @param name                         a
     * @param useAllBody                   a
     * @param required                     a
     * @param javaType                     a
     * @param scxMappingRoutingContextInfo a
     * @return a
     * @throws cool.scx.mvc.parameter_handler.RequiredParamEmptyException a
     * @throws cool.scx.mvc.parameter_handler.ParamConvertException       a
     */
    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, Type javaType, ScxMappingRoutingContextInfo scxMappingRoutingContextInfo) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = scxMappingRoutingContextInfo.getBody();
        if (!useAllBody) {
            var split = name.split("\\.");
            for (var s : split) {
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
            return readValue(tempValue, javaType);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * a
     *
     * @param jsonNode a
     * @param type     a
     * @param <T>      a
     * @return a
     * @throws java.io.IOException a
     */
    private static <T> T readValue(JsonNode jsonNode, Type type) throws IOException {
        return ObjectUtils.jsonMapper().readerFor(ObjectUtils.constructType(type)).readValue(jsonNode);
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
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo scxMappingRoutingContextInfo) throws Exception {
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

        return getValueFromBody(name, useAllBody, required, javaType, scxMappingRoutingContextInfo);
    }

}
