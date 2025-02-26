package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static cool.scx.reflect.MethodInfoHelper.*;
import static cool.scx.reflect.ReflectHelper._findAccessModifier;

/// MethodInfo
///
/// @author scx567888
/// @version 0.0.1
public final class MethodInfo implements IMethodInfo {

    private final Method method;
    private final ClassInfo classInfo;
    private final String name;
    private final boolean isAbstract;
    private final boolean isStatic;
    private final AccessModifier accessModifier;

    private final Annotation[] annotations;
    private final JavaType returnType;

    private final ParameterInfo[] parameters;
    private final IMethodInfo superMethod;
    private final Annotation[] allAnnotations;

    MethodInfo(Method method, ClassInfo classInfo) {
        this.method = method;
        this.classInfo = classInfo;
        var accessFlags = method.accessFlags();
        this.name = _findName(method);
        this.isAbstract = _isAbstract(accessFlags);
        this.isStatic = _isStatic(accessFlags);
        this.accessModifier = _findAccessModifier(accessFlags);
        this.annotations = _findAnnotations(method);
        this.returnType = _findReturnType(this);
        this.parameters = _findParameterInfos(this);
        this.superMethod = _findSuperMethod(this);
        this.allAnnotations = _findAllAnnotations(this);

    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public ClassInfo classInfo() {
        return classInfo;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public JavaType returnType() {
        return returnType;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    @Override
    public IMethodInfo superMethod() {
        return superMethod;
    }

    /**
     * 获取当前方法的注解 以及 重写的父类方法的注解
     */
    @Override
    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    @Override
    public void setAccessible(boolean flag) {
        this.method.setAccessible(flag);
    }

}
