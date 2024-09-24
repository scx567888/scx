package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.ReflectFactory.*;

/**
 * FieldInfo
 */
public final class FieldInfo {

    private final Field field;
    private final ClassInfo classInfo;
    private final String name;
    private final AccessModifier accessModifier;
    private final JavaType type;
    private final Annotation[] annotations;

    FieldInfo(Field field, ClassInfo classInfo) {
        this.field = field;
        this.classInfo = classInfo;
        this.name = _findName(this);
        this.accessModifier = _findAccessModifier(this);
        this.type = _findType(this);
        this.annotations = _findAnnotations(this);
    }

    public Field field() {
        return this.field;
    }

    public ClassInfo classInfo() {
        return this.classInfo;
    }

    public String name() {
        return name;
    }

    public AccessModifier accessModifier() {
        return this.accessModifier;
    }

    public JavaType type() {
        return this.type;
    }

    public Annotation[] annotations() {
        return this.annotations;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return this.field.getAnnotation(annotationClass);
    }

    public Annotation[] getAnnotations() {
        return this.field.getAnnotations();
    }

    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return this.field.getAnnotationsByType(annotationClass);
    }

    public void setAccessible(boolean flag) {
        this.field.setAccessible(flag);
    }

    public void set(Object obj, Object value) throws IllegalAccessException {
        this.field.set(obj, value);
    }

    public Object get(Object obj) throws IllegalAccessException {
        return this.field.get(obj);
    }

}
