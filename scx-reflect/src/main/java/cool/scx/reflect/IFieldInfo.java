package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/// 仅用作规范
public interface IFieldInfo {

    Field field();

    IClassInfo classInfo();

    String name();

    AccessModifier accessModifier();

    JavaType type();

    Annotation[] annotations();

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    Annotation[] getAnnotations();

    <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass);

    void setAccessible(boolean flag);

    void set(Object obj, Object value) throws IllegalAccessException;

    Object get(Object obj) throws IllegalAccessException;
    
}
