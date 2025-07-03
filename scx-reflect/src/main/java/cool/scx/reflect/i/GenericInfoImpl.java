package cool.scx.reflect.i;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class GenericInfoImpl implements GenericInfo {

    private final String name;
    private final ClassInfo actualType;

    public GenericInfoImpl(TypeVariable<?> typeVariable, Type type) {
        this.name = typeVariable.getName();
        this.actualType = ScxReflect.getClassInfo(type);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ClassInfo[] upperBounds() {
        return new ClassInfo[0];
    }

    @Override
    public ClassInfo[] lowerBounds() {
        return new ClassInfo[0];
    }

    @Override
    public ClassInfo actualType() {
        return actualType;
    }

}
