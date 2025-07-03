package cool.scx.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static cool.scx.reflect.ReflectHelper._findUpperBounds;

public class GenericInfoImpl implements GenericInfo {

    private final String name;
    private final ClassInfo[] upperBounds;
    private final ClassInfo[] lowerBounds;
    private final TypeInfo actualType;

    public GenericInfoImpl(TypeVariable<?> typeVariable, Type type) {
        this.name = typeVariable.getName();
        this.upperBounds = _findUpperBounds(typeVariable);
        this.lowerBounds = new ClassInfo[0];
        this.actualType = ScxReflect.getTypeInfo(type);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ClassInfo[] upperBounds() {
        return upperBounds;
    }

    @Override
    public ClassInfo[] lowerBounds() {
        return lowerBounds;
    }

    @Override
    public TypeInfo actualType() {
        return actualType;
    }

}
