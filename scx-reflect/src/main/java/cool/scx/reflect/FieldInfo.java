package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.FieldInfoHelper.*;
import static cool.scx.reflect.ReflectHelper._findAccessModifier;


/// FieldInfo
///
/// @author scx567888
/// @version 0.0.1
public final class FieldInfo implements IFieldInfo {

    private final Field field;
    private final ClassInfo classInfo;
    private final String name;
    private final AccessModifier accessModifier;
    private final JavaType type;
    private final Annotation[] annotations;

    FieldInfo(Field field, ClassInfo classInfo) {
        this.field = field;
        this.classInfo = classInfo;
        var accessFlags = field.accessFlags();
        this.name = _findName(field);
        this.accessModifier = _findAccessModifier(accessFlags);
        this.type = _findType(this);
        this.annotations = _findAnnotations(field);
    }

    @Override
    public Field field() {
        return this.field;
    }

    @Override
    public ClassInfo classInfo() {
        return this.classInfo;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public AccessModifier accessModifier() {
        return this.accessModifier;
    }

    @Override
    public JavaType type() {
        return this.type;
    }

    @Override
    public Annotation[] annotations() {
        return this.annotations;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return this.field.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.field.getAnnotations();
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return this.field.getAnnotationsByType(annotationClass);
    }

    @Override
    public void setAccessible(boolean flag) {
        this.field.setAccessible(flag);
    }

    @Override
    public void set(Object obj, Object value) throws IllegalAccessException {
        this.field.set(obj, value);
    }

    @Override
    public Object get(Object obj) throws IllegalAccessException {
        return this.field.get(obj);
    }

}
