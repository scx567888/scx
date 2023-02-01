package cool.scx.mvc.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.mvc.ScxMappingRoutingContextInfo;
import cool.scx.mvc.ScxMvcParameterHandler;
import cool.scx.mvc.annotation.FromPath;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;

import static cool.scx.mvc.ScxMvcHelper.getFromMap;
import static cool.scx.util.ObjectUtils.*;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromPathMethodParameterHandler implements ScxMvcParameterHandler {

    /**
     * a
     *
     * @param name     a
     * @param merge    a
     * @param required a
     * @param javaType a
     * @param info     a
     * @return a
     * @throws RequiredParamEmptyException a
     * @throws ParamConvertException       a
     */
    public static Object getValueFromPath(String name, boolean merge, boolean required, JavaType javaType, ScxMappingRoutingContextInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, info.routingContext().pathParams(), merge);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = convertValue(tempValue, javaType, Option.IGNORE_JSON_IGNORE);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromPath.class) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMappingRoutingContextInfo info) throws Exception {
        var javaType = constructType(parameter.getParameterizedType());
        var required = false;
        var name = parameter.getName();
        var merge = false;

        var fromPath = parameter.getAnnotation(FromPath.class);
        if (fromPath != null) {
            required = fromPath.required();
            if (StringUtils.notBlank(fromPath.value())) {
                name = fromPath.value();
            }
            merge = fromPath.merge();
        }

        return getValueFromPath(name, merge, required, javaType, info);
    }

}
