package cool.scx.web.parameter_handler.from_query;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromQuery;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import static cool.scx.common.constant.AnnotationValueHelper.getRealValue;
import static cool.scx.common.util.ObjectUtils.constructType;
import static cool.scx.web.parameter_handler.from_body.FromBodyParameterHandler.readValue;

/// FromQueryParameterHandler
///
/// @author scx567888
/// @version 0.0.1
public final class FromQueryParameterHandler implements ParameterHandler {

    private final FromQuery fromQuery;
    private final ParameterInfo parameter;
    private final String value;

    public FromQueryParameterHandler(FromQuery fromQuery, ParameterInfo parameter) {
        this.fromQuery = fromQuery;
        this.parameter = parameter;
        var tempValue = getRealValue(fromQuery.value());
        this.value = tempValue != null ? tempValue : parameter.name();
    }

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
    public Object handle(RequestInfo requestInfo) throws Exception {
        return getValueFromQuery(value, fromQuery.merge(), fromQuery.required(), constructType(parameter.rawParameter().getParameterizedType()), requestInfo);
    }

}
