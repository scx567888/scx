package cool.scx.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static cool.scx.reflect.ConstructorInfoHelper._findParameterInfos;
import static cool.scx.reflect.ReflectHelper._findAccessModifier;

/// ConstructorInfo
///
/// @author scx567888
/// @version 0.0.1
final class ConstructorInfo implements IConstructorInfo {

    private final Constructor<?> constructor;
    private final ClassInfo classInfo;
    private final AccessModifier accessModifier;
    private final ParameterInfo[] parameters;

    ConstructorInfo(Constructor<?> constructor, ClassInfo classInfo) {
        this.constructor = constructor;
        this.classInfo = classInfo;
        var accessFlags = constructor.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.parameters = _findParameterInfos(this);
    }

    @Override
    public Constructor<?> constructor() {
        return constructor;
    }

    @Override
    public ClassInfo classInfo() {
        return classInfo;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    @Override
    public void setAccessible(boolean flag) {
        constructor.setAccessible(flag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) constructor.newInstance(args);
    }

}
