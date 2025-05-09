package cool.scx.bean.dependency;

import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ParameterInfo;

public record DependencyContext(
        Type type,
        Class<?> beanClass, boolean singleton, FieldInfo fieldInfo,
        ConstructorInfo constructor, ParameterInfo parameter
) {

    public DependencyContext(Class<?> beanClass, boolean singleton, FieldInfo fieldInfo) {
        this(Type.FIELD, beanClass, singleton, fieldInfo, null, null);
    }

    public DependencyContext(Class<?> beanClass, ConstructorInfo constructor, ParameterInfo parameter) {
        this(Type.CONSTRUCTOR, beanClass, false, null, constructor, parameter);
    }

    public enum Type {
        CONSTRUCTOR,
        FIELD,
    }

}
