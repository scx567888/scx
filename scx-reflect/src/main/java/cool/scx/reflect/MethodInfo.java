package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static cool.scx.reflect.ReflectHelper.*;
import static java.lang.reflect.AccessFlag.FINAL;

/// MethodInfo
///
/// @author scx567888
/// @version 0.0.1
public final class MethodInfo implements ExecutableInfo {

    private final Method method;
    private final ClassInfo classInfo;
    private final String name;
    private final AccessModifier accessModifier;
    private final MethodType methodType;
    private final JavaType returnType;
    private final ParameterInfo[] parameters;
    private final MethodInfo superMethod;
    private final Annotation[] annotations;
    private final Annotation[] allAnnotations;
    private final boolean isFinal;

    MethodInfo(Method method, ClassInfo classInfo) {
        this.method = method;
        this.classInfo = classInfo;
        this.name = method.getName();
        var accessFlags = method.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.methodType = _findMethodType(method, accessFlags);
        this.returnType = _findType(method.getGenericReturnType(), classInfo);
        this.parameters = _findParameterInfos(this);
        this.superMethod = _findSuperMethod(this);
        this.annotations = method.getDeclaredAnnotations();
        this.allAnnotations = _findAllAnnotations(this);
        this.isFinal = accessFlags.contains(FINAL);
    }

    public Method method() {
        return method;
    }

    public String name() {
        return name;
    }

    public MethodType methodType() {
        return methodType;
    }

    public JavaType returnType() {
        return returnType;
    }

    public MethodInfo superMethod() {
        return superMethod;
    }

    public Annotation[] annotations() {
        return annotations;
    }

    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    public boolean isFinal() {
        return isFinal;
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
        method.setAccessible(flag);
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

}
