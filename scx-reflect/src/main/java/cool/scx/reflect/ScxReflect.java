package cool.scx.reflect;

import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ScxReflect {

    static final Map<Type, TypeInfo> TYPE_INFO_CACHE = new ConcurrentHashMap<>();

    //todo 多线程问题 ? 
    public static TypeInfo getTypeInfo(Type type) {
        var typeInfo = TYPE_INFO_CACHE.get(type);
        if (typeInfo != null) {
            return typeInfo;
        }
        if (type instanceof Class<?> || type instanceof ParameterizedType || type instanceof GenericArrayType) {
            return new ClassInfoImpl(type);
        }
        if (type instanceof TypeVariable<?> typeVariable) {
            return new TypeVariableInfoImpl(typeVariable);
        }
        if (type instanceof WildcardType wildcardType) {
            return new WildcardTypeInfoImpl(wildcardType);
        }
        throw new IllegalArgumentException("unsupported type: " + type);
    }

    public static ClassInfo getClassInfo(Class<?> clazz) {
        // 这里强转是安全的 
        return (ClassInfo) getTypeInfo(clazz);
    }

}
