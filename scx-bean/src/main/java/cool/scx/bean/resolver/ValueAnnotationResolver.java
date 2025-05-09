package cool.scx.bean.resolver;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.bean.annotation.Value;
import cool.scx.bean.exception.MissingValueException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.reflect.AnnotatedElementInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ParameterInfo;

import java.util.Map;

/// 处理 Value 注解
public class ValueAnnotationResolver implements BeanResolver {

    private final Map<String, Object> map;

    public ValueAnnotationResolver(Map<String, Object> map) {
        this.map = map;
    }

    public Object resolveValue(AnnotatedElementInfo annotatedElementInfo, JavaType javaType) throws MissingValueException {
        var annotation = annotatedElementInfo.findAnnotation(Value.class);
        if (annotation == null) {
            return null;
        }
        var rawValue = map.get(annotation.value());
        if (rawValue == null) {
            throw new MissingValueException("未找到 @Value 值 " + annotation.value());
        }
        return ObjectUtils.convertValue(rawValue, javaType);
    }

    @Override
    public Object resolveConstructorArgument(ParameterInfo parameterInfo) {
        return resolveValue(parameterInfo, parameterInfo.type());
    }

    @Override
    public Object resolveFieldValue(FieldInfo fieldInfo) {
        return resolveValue(fieldInfo, fieldInfo.type());
    }

}
