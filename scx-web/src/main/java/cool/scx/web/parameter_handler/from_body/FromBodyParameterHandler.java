package cool.scx.web.parameter_handler.from_body;

import cool.scx.object.jackson.JsonNodeHelper;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.TypeInfo;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import java.io.IOException;

import static cool.scx.common.constant.AnnotationValues.getRealValue;

/// FromBodyParameterHandler
///
/// @author scx567888
/// @version 0.0.1
public final class FromBodyParameterHandler implements ParameterHandler {

    private final FromBody fromBody;
    private final ParameterInfo parameter;
    private final String value;

    public FromBodyParameterHandler(FromBody fromBody, ParameterInfo parameter) {
        this.fromBody = fromBody;
        this.parameter = parameter;
        var tempValue = getRealValue(fromBody.value());
        this.value = tempValue != null ? tempValue : parameter.name();
    }

    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, TypeInfo javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        return fromBody(name, useAllBody, required, javaType, info);
    }

    private static Object fromBody(String name, boolean useAllBody, boolean required, TypeInfo javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = useAllBody ? info.body() : JsonNodeHelper.get(info.body(), name);
        // 为了提高性能这里提前做一次校验
        if (tempValue == null || tempValue instanceof NullNode) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.toString() + "]");
            }
            return null;
        }
        Object o;
        try {
            o = readValue(tempValue, javaType);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.toString() + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType.toString() + "]");
        }
        return o;
    }

    public static <T> T readValue(Node jsonNode, TypeInfo type) throws IOException {
        //todo 
//        var reader = jsonMapper(new Options().setIgnoreJsonIgnore(true)).readerFor(type);
        //这里我们做一下数组的兼容 保证无论参数是数组还是type是数组都可以正确读取
//        if (jsonNode.isArray()) {
//            if (type.isArrayType() || type.isCollectionLikeType()) {
//                return reader.readValue(jsonNode);
//            } else {
//                return reader.readValue(jsonNode.get(0));
//            }
//        } else {
//            if (type.isArrayType() || type.isCollectionLikeType()) {
//                var add = jsonMapper().createArrayNode().add(jsonNode);
//                return reader.readValue(add);
//            } else {
//                return reader.readValue(jsonNode);
//            }
//        }
        return null;
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
        return getValueFromBody(value, fromBody.useAllBody(), fromBody.required(), parameter.parameterType(), requestInfo);
    }

}
