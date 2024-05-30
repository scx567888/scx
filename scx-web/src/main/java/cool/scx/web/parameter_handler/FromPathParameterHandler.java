package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.common.reflect.ParameterInfo;
import cool.scx.web.annotation.FromPath;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.common.util.ObjectUtils.Options;
import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.web.ScxWebHelper.getFromMap;

/**
 * FromPathParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromPathParameterHandler implements ParameterHandler {

    public static Object getValueFromPath(String name, boolean merge, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = getFromMap(name, info.routingContext().pathParams(), merge);
        if (tempValue == null) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = convertValue(tempValue, javaType, new Options().setIgnoreJsonIgnore(true));
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromPath, merge=" + merge + "] , 参数类型 [" + javaType.getTypeName() + "]");
        }
        return o;
    }

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.parameter().getAnnotation(FromPath.class) != null;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var fromPath = parameter.parameter().getAnnotation(FromPath.class);
        if (fromPath == null) {
            throw new IllegalArgumentException("参数没有 FromPath 注解 !!!");
        }
        var value = getAnnotationValue(fromPath.value());
        return getValueFromPath(value != null ? value : parameter.name(), fromPath.merge(), fromPath.required(), parameter.type(), requestInfo);
    }

}
