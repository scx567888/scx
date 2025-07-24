package cool.scx.web.parameter_handler.from_body;

import cool.scx.object.NodeHelper;
import cool.scx.object.ScxObject;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.TypeInfo;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import java.io.IOException;

import static cool.scx.common.constant.AnnotationValues.getRealValue;
import static cool.scx.web.parameter_handler.from_upload.FromUploadParameterHandler.checkIsArray;
import static cool.scx.web.parameter_handler.from_upload.FromUploadParameterHandler.checkIsCollection;

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
        var tempValue = useAllBody ? info.body() : NodeHelper.get(info.body(), name);
        // 为了提高性能这里提前做一次校验
        if (tempValue == null || tempValue.isNull()) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType + "]");
            }
            return null;
        }
        Object o;
        try {
            o = readValue(tempValue, javaType);
        } catch (Exception e) {
            throw new ParamConvertException("参数类型转换异常 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType + "] , 详细错误信息 : " + e.getMessage());
        }
        if (o == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromBody, useAllBody=" + useAllBody + "] , 参数类型 [" + javaType + "]");
        }
        return o;
    }

    public static <T> T readValue(Node jsonNode, TypeInfo type) throws IOException {
        //这里我们做一下数组的兼容 保证无论参数是数组还是type是数组都可以正确读取
        if (jsonNode instanceof ArrayNode arrayNode) {
            if (checkIsCollection(type) || checkIsArray(type)) {
                return ScxObject.convertValue(arrayNode, type);
            } else {
                return ScxObject.convertValue(arrayNode.get(0), type);
            }
        } else {
            if (checkIsCollection(type) || checkIsArray(type)) {
                var arrayNode = new ArrayNode();
                arrayNode.add(jsonNode);
                return ScxObject.convertValue(arrayNode, type);
            } else {
                return ScxObject.convertValue(jsonNode, type);
            }
        }
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
        // todo 这里可能有问题
        return getValueFromBody(value, fromBody.useAllBody(), fromBody.required(), parameter.parameterType(), requestInfo);
    }

}
