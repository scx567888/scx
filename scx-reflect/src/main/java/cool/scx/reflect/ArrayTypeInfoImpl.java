package cool.scx.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import static cool.scx.reflect.ReflectSupport._findComponentType;
import static cool.scx.reflect.ScxReflect.TYPE_CACHE;

/// ArrayTypeInfoImpl
///
/// @author scx567888
/// @version 0.0.1
final class ArrayTypeInfoImpl implements ArrayTypeInfo {

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
