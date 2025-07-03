package cool.scx.reflect.i;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static cool.scx.reflect.i.ClassInfoHelper._findAccessModifier;
import static cool.scx.reflect.i.ConstructorInfoHelper._findParameters;

public class MethodInfoImpl implements MethodInfo {

    private final Method rawMethod;
    private final ClassInfo declaringClass;
    private final String name;
    private final AccessModifier accessModifier;
    private final ParameterInfo[] parameters;
    private final ClassInfo returnType;
    private final Annotation[] annotations;

    public MethodInfoImpl(Method method, ClassInfo declaringClass) {
        this.rawMethod = method;
        this.declaringClass = declaringClass;
        this.name = method.getName();
        var accessFlags = method.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.parameters = _findParameters(this.rawMethod, this);
        this.returnType = ScxReflect.getClassInfo(method.getGenericReturnType());
        this.annotations = method.getDeclaredAnnotations();
    }

    @Override
    public Method rawMethod() {
        return rawMethod;
    }

    @Override
    public ClassInfo declaringClass() {
        return declaringClass;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ClassInfo returnType() {
        return returnType;
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
