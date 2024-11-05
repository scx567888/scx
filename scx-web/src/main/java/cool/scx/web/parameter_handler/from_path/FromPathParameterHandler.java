package cool.scx.web.parameter_handler.from_path;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromPath;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.web.parameter_handler.FromBodyParameterHandler.readValue;

/**
 * FromPathParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromPathParameterHandler implements ParameterHandler {

    private final FromPath fromPath;
    private final ParameterInfo parameter;
    private final String value;

    public FromPathParameterHandler(FromPath fromPath, ParameterInfo parameter) {
        this.fromPath = fromPath;
        this.parameter = parameter;
        var tempValue = getAnnotationValue(fromPath.value());
        this.value = tempValue != null ? tempValue : parameter.name();
    }

    public static Object getValueFromPath(String name, boolean merge, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = merge ? info.pathParams() : info.pathParams().get(name);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = readValue(tempValue, javaType);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
        return getValueFromPath(value, fromPath.merge(), fromPath.required(), parameter.type(), requestInfo);
    }

}
