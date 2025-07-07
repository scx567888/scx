package cool.scx.reflect.impl;

import cool.scx.reflect.TypeInfo;

import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TypeFactory {

    static final Map<Type, TypeInfo> TYPE_CACHE = new ConcurrentHashMap<>();

    // todo 此方法现在非线程安全
    static TypeInfo getType(Type type, Map<TypeVariable<?>, TypeInfo> bindings) {
        var t = TYPE_CACHE.get(type);
        if (t != null) {
            return t;
        }
        if (type instanceof Class<?> c) {
            if (c.isArray()) {
                return new ArrayTypeInfoImpl(c, bindings);
            }
            if (c.isPrimitive()) {
                return new PrimitiveTypeInfoImpl(c);
            }
            return new ClassInfoImpl(type, bindings);
        }
        if (type instanceof ParameterizedType) {
            return new ClassInfoImpl(type, bindings);
        }
        if (type instanceof GenericArrayType g) {
            return new ArrayTypeInfoImpl(g, bindings);
        }
        if (type instanceof TypeVariable<?> typeVariable) {
            //尝试从从绑定中获取 否则回退到 上界
            var typeInfo = bindings.get(typeVariable);
            if (typeInfo != null) {
                return typeInfo;
            }
            return getType(typeVariable.getBounds()[0], bindings);
        }
        if (type instanceof WildcardType wildcardType) {
            //直接回退到上界
            return getType(wildcardType.getUpperBounds()[0], bindings);
        }
        throw new IllegalArgumentException("unsupported type: " + type);
    }

    public static TypeInfo getType(TypeReference<?> typeReference) {
        return getType(typeReference.getType());
    }

    public static TypeInfo getType(Type type) {
        return getType(type, Map.of());
    }

}
