package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/// 仅用做规范
public interface IMethodInfo extends IExecutableInfo {

    Method method();

    String name();

    boolean isAbstract();

    boolean isStatic();

    AccessModifier accessModifier();

    Annotation[] annotations();

    JavaType returnType();

    IMethodInfo superMethod();

    /// 获取当前方法的注解 以及 重写的父类方法的注解
    Annotation[] allAnnotations();

    void setAccessible(boolean flag);

}
