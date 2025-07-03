package cool.scx.reflect.i;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import static cool.scx.reflect.i.ClassInfoHelper._findAccessModifier;
import static cool.scx.reflect.i.ConstructorInfoHelper._findParameters;

public class ConstructorInfoImpl implements ConstructorInfo {

    private final Constructor<?> rawConstructor;
    private final ClassInfo declaringClass;
    private final AccessModifier accessModifier;
    private final ParameterInfo[] parameters;
    private final Annotation[] annotations;

    public ConstructorInfoImpl(Constructor<?> constructor, ClassInfo declaringClass) {
        this.rawConstructor = constructor;
        this.declaringClass = declaringClass;
        var accessFlags = constructor.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.parameters = _findParameters(this.rawConstructor, this);
        this.annotations = constructor.getDeclaredAnnotations();
    }

    @Override
    public Constructor<?> rawConstructor() {
        return rawConstructor;
    }

    @Override
    public ClassInfo declaringClass() {
        return declaringClass;
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
    public Annotation[] annotations() {
        return annotations;
    }

}
