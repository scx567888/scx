package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.Helper._findAccessModifier;
import static cool.scx.reflect.Helper._findType;


/// FieldInfo
///
/// @author scx567888
/// @version 0.0.1
public final class FieldInfo implements MemberInfo {

    private final Field field;
    private final ClassInfo classInfo;
    private final AccessModifier accessModifier;
    private final String name;
    private final JavaType type;
    private final Annotation[] annotations;

    FieldInfo(Field field, ClassInfo classInfo) {
        this.field = field;
        this.classInfo = classInfo;
        this.accessModifier = _findAccessModifier(field.accessFlags());
        this.name = field.getName();
        this.type = _findType(field.getGenericType(), classInfo);
        this.annotations = field.getDeclaredAnnotations();
    }

    public Field field() {
        return field;
    }

    public String name() {
        return name;
    }

    public JavaType type() {
        return type;
    }

    public Annotation[] annotations() {
        return this.annotations;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        for (var annotation : this.annotations) {
            if (annotationClass.isInstance(annotation)) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

    public void set(Object obj, Object value) throws IllegalAccessException {
        field.set(obj, value);
    }

    public Object get(Object obj) throws IllegalAccessException {
        return field.get(obj);
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
        this.field.setAccessible(flag);
    }

}
