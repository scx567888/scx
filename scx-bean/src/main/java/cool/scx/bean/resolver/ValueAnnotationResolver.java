package cool.scx.bean.resolver;

import cool.scx.bean.annotation.Value;
import cool.scx.bean.exception.MissingValueException;
import cool.scx.object.ScxObject;
import cool.scx.reflect.AnnotatedElementInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.TypeInfo;

import java.util.Map;

/// 处理 Value 注解
///
/// @author scx567888
/// @version 0.0.1
public class ValueAnnotationResolver implements BeanResolver {

    private final Map<String, Object> map;

    public ValueAnnotationResolver(Map<String, Object> map) {
        this.map = map;
    }

    public Object resolveValue(AnnotatedElementInfo annotatedElementInfo, TypeInfo javaType) throws MissingValueException {
        var annotation = annotatedElementInfo.findAnnotation(Value.class);
        if (annotation == null) {
            return null;
        }
        var rawValue = map.get(annotation.value());
        if (rawValue == null) {
            throw new MissingValueException("未找到 @Value 值 " + annotation.value());
        }
        return ScxObject.convertValue(rawValue, javaType);
    }

    @Override
    public Object resolveConstructorArgument(ParameterInfo parameterInfo) {
        return resolveValue(parameterInfo, parameterInfo.parameterType());
    }

    @Override
    public Object resolveFieldValue(FieldInfo fieldInfo) {
        return resolveValue(fieldInfo, fieldInfo.fieldType());
    }

}
