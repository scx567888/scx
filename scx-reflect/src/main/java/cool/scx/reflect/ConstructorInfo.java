package cool.scx.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static cool.scx.reflect.Helper._findAccessModifier;
import static cool.scx.reflect.Helper._findParameterInfos;

/// ConstructorInfo
///
/// @author scx567888
/// @version 0.0.1
public final class ConstructorInfo implements ExecutableInfo {

    private final Constructor<?> constructor;
    private final ClassInfo classInfo;
    private final AccessModifier accessModifier;
    private final ParameterInfo[] parameters;

    ConstructorInfo(Constructor<?> constructor, ClassInfo classInfo) {
        this.constructor = constructor;
        this.classInfo = classInfo;
        this.accessModifier = _findAccessModifier(constructor.accessFlags());
        this.parameters = _findParameterInfos(this);
    }

    public Constructor<?> constructor() {
        return constructor;
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance(Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) constructor.newInstance(args);
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
    public void setAccessible(boolean flag) {
        constructor.setAccessible(flag);
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

}
