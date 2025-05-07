package cool.scx.bean.x;

import cool.scx.bean.x.annotation.Value;
import cool.scx.common.util.ObjectUtils;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.MethodInfo;
import cool.scx.reflect.ParameterInfo;

import java.util.Map;

/// 处理 value 注解
public class ValueAnnotationResolver implements BeanDependencyResolver {

    private final Map<String, Object> map;

    public ValueAnnotationResolver(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public Object resolveConstructorArgument(ParameterInfo parameterInfo) {
        var annotation = parameterInfo.findAnnotation(Value.class);
        if (annotation == null) {
            return null;
        }
        var rawValue = map.get(annotation.value());
        return ObjectUtils.convertValue(rawValue, parameterInfo.type());
    }

    @Override
    public Object resolveFieldValue(FieldInfo fieldInfo) {
        var annotation = fieldInfo.findAnnotation(Value.class);
        if (annotation == null) {
            return null;
        }
        var rawValue = map.get(annotation.value());
        return ObjectUtils.convertValue(rawValue, fieldInfo.type());
    }

    @Override
    public boolean resolveMethod(MethodInfo methodInfo) {
        return false;
    }

}
