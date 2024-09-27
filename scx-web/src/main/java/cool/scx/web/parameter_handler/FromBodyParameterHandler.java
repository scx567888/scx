package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.parameter_handler.exception.ParamConvertException;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import java.io.IOException;

import static cool.scx.common.util.AnnotationUtils.getAnnotationValue;
import static cool.scx.common.util.ObjectUtils.Options;
import static cool.scx.common.util.ObjectUtils.jsonMapper;

/**
 * FromBodyParameterHandler
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromBodyParameterHandler implements ParameterHandler {

    public static Object getValueFromBody(String name, boolean useAllBody, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        return fromBody(name, useAllBody, required, javaType, info);
    }

    private static Object fromBody(String name, boolean useAllBody, boolean required, JavaType javaType, RequestInfo info) throws RequiredParamEmptyException, ParamConvertException {
        var tempValue = useAllBody ? info.body() : JsonNodeHelper.get(info.body(), name);
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

    public static <T> T readValue(JsonNode jsonNode, JavaType type) throws IOException {
        var reader = jsonMapper(new Options().setIgnoreJsonIgnore(true)).readerFor(type);
        //这里我们做一下数组的兼容 保证无论参数是数组还是type是数组都可以正确读取
        if (jsonNode.isArray()) {
            if (type.isArrayType() || type.isCollectionLikeType()) {
                return reader.readValue(jsonNode);
            } else {
                return reader.readValue(jsonNode.get(0));
            }
        } else {
            if (type.isArrayType() || type.isCollectionLikeType()) {
                var add = jsonMapper().createArrayNode().add(jsonNode);
                return reader.readValue(add);
            } else {
                return reader.readValue(jsonNode);
            }
        }
    }

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        return parameter.parameter().getAnnotation(FromBody.class) != null;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws Exception {
        var fromBody = parameter.parameter().getAnnotation(FromBody.class);
        if (fromBody == null) {
            throw new IllegalArgumentException("参数没有 FromBody 注解 !!!");
        }
        var value = getAnnotationValue(fromBody.value());
        return getValueFromBody(value != null ? value : parameter.name(), fromBody.useAllBody(), fromBody.required(), parameter.type(), requestInfo);
    }

}
