package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromQuery;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.web.parameter_handler.FromBodyParameterHandler.readValue;

/**
 * FromQueryParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromQueryParameterHandler implements ParameterHandler {

    public static Object getValueFromQuery(String name, boolean merge, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = merge ? info.query() : info.query().get(name);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = readValue(tempValue, javaType);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromQuery, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.parameter().getAnnotation(FromQuery.class) != null;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var fromQuery = parameter.parameter().getAnnotation(FromQuery.class);
        if (fromQuery == null) {
            throw new IllegalArgumentException("参数没有 FromQuery 注解 !!!");
        }
        var value = getAnnotationValue(fromQuery.value());
        return getValueFromQuery(value != null ? value : parameter.name(), fromQuery.merge(), fromQuery.required(), parameter.type(), requestInfo);
    }

}
