package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static cool.scx.reflect.ReflectFactory.*;

/**
 * MethodInfo
 */
public final class MethodInfo implements ExecutableInfo {

    private final Method method;
    private final ClassInfo classInfo;
    private final String name;
    private final boolean isAbstract;
    private final AccessModifier accessModifier;

    private final Annotation[] annotations;
    private final JavaType returnType;

    private final ParameterInfo[] parameters;
    private final MethodInfo superMethod;
    private final Annotation[] allAnnotations;

    MethodInfo(Method method, ClassInfo classInfo) {
        this.method = method;
        this.classInfo = classInfo;
        this.name = _findName(this);
        this.isAbstract = _isAbstract(this);
        this.accessModifier = _findAccessModifier(this);
        this.annotations = _findAnnotations(this);
        this.returnType = _findReturnType(this);
        this.parameters = _findParameterInfos(this);
        this.superMethod = _findSuperMethod(this);
        this.allAnnotations = _findAllAnnotations(this);

    }

    public Method method() {
        return method;
    }

    @Override
    public ClassInfo classInfo() {
        return classInfo;
    }

    public String name() {
        return name;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public AccessModifier accessModifier() {
        return accessModifier;
    }

    public Annotation[] annotations() {
        return annotations;
    }

    public JavaType returnType() {
        return returnType;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    public MethodInfo superMethod() {
        return superMethod;
    }

    /**
     * 获取当前方法的注解 以及 重写的父类方法的注解
     */
    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    public void setAccessible(boolean flag) {
        this.method.setAccessible(flag);
    }

}
