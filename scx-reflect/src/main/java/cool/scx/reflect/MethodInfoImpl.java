package cool.scx.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;

import static cool.scx.reflect.ReflectHelper.*;

public final class MethodInfoImpl implements MethodInfo {

    private final Method rawMethod;
    private final ClassInfo declaringClass;
    private final String name;
    private final AccessModifier accessModifier;
    private final MethodType methodType;
    private final boolean isFinal;
    private final boolean isStatic;
    private final ParameterInfo[] parameters;
    private final TypeInfo returnType;
    private final Annotation[] annotations;

    public MethodInfoImpl(Method method, ClassInfo declaringClass) {
        this.rawMethod = method;
        this.declaringClass = declaringClass;
        this.name = method.getName();
        var accessFlags = method.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.methodType = _findMethodType(this.rawMethod, accessFlags);
        this.isFinal = accessFlags.contains(AccessFlag.FINAL);
        this.isStatic = accessFlags.contains(AccessFlag.STATIC);
        this.parameters = _findParameters(this.rawMethod, this);
        this.returnType = ScxReflect.getTypeInfo(method.getGenericReturnType());
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
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public MethodType methodType() {
        return methodType;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    @Override
    public TypeInfo returnType() {
        return returnType;
    }

    @Override
    public MethodInfo superMethod() {
        //todo 待完成
        return null;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

}
