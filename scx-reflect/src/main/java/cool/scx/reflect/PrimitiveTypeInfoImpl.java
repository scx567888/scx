package cool.scx.reflect;

import static cool.scx.reflect.ScxReflect.TYPE_CACHE;

/// 基本类型
final class PrimitiveTypeInfoImpl implements PrimitiveTypeInfo {

    private final Class<?> primitiveClass;

    PrimitiveTypeInfoImpl(Class<?> primitiveClass) {
        TYPE_CACHE.put(primitiveClass, this);
        // 这里假设 primitiveClass 已经 过滤过了
        this.primitiveClass = primitiveClass;
    }

    @Override
    public Class<?> primitiveClass() {
        return primitiveClass;
    }

    @Override
    public String toString() {
        return primitiveClass.getName();
    }

}
