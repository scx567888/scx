package cool.scx.mvc.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.mvc.ScxMvcParameterHandler;
import cool.scx.mvc.ScxMvcRequestInfo;
import cool.scx.mvc.annotation.FromQuery;
import cool.scx.mvc.parameter_handler.exception.ParamConvertException;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.common.util.reflect.AnnotationUtils;

import java.lang.reflect.Parameter;

import static cool.scx.mvc.ScxMvcHelper.getFromMap;
import static cool.scx.common.util.ObjectUtils.*;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromQueryParameterHandler implements ScxMvcParameterHandler {

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
    public static Object getValueFromQuery(String name, boolean merge, boolean required, JavaType javaType, ScxMvcRequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, info.routingContext().queryParams(), merge, javaType);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = convertValue(tempValue, javaType, Option.IGNORE_JSON_IGNORE);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        return parameter.getAnnotation(FromQuery.class) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Parameter parameter, ScxMvcRequestInfo info) throws Exception {
        var javaType = constructType(parameter.getParameterizedType());
        var required = false;
        var name = parameter.getName();
        var merge = false;

        var fromQuery = parameter.getAnnotation(FromQuery.class);
        if (fromQuery != null) {
            required = fromQuery.required();
            var _value = AnnotationUtils.getAnnotationValue(fromQuery.value());
            if (_value != null) {
                name = _value;
            }
            merge = fromQuery.merge();
        }

        return getValueFromQuery(name, merge, required, javaType, info);
    }

}
