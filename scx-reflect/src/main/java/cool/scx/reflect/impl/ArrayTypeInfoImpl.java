package cool.scx.reflect.impl;

import cool.scx.reflect.ArrayTypeInfo;
import cool.scx.reflect.TypeInfo;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import static cool.scx.reflect.impl.ReflectHelper._findComponentType;
import static cool.scx.reflect.impl.TypeFactory.TYPE_CACHE;

/// ArrayTypeInfoImpl
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayTypeInfoImpl implements ArrayTypeInfo {

    private final TypeInfo componentType;

    ArrayTypeInfoImpl(Type type, Map<TypeVariable<?>, TypeInfo> bindings) {
        TYPE_CACHE.put(type, this);
        this.componentType = _findComponentType(type, bindings);
    }

    @Override
    public TypeInfo componentType() {
        return componentType;
    }

    @Override
    public String toString() {
        return componentType.toString() + "[]";
    }

}
