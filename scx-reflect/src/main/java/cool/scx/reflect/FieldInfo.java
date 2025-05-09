package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.ReflectHelper._findAccessModifier;
import static cool.scx.reflect.ReflectHelper._findType;
import static java.lang.reflect.AccessFlag.FINAL;


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
    private final boolean isFinal;

    FieldInfo(Field field, ClassInfo classInfo) {
        this.field = field;
        this.classInfo = classInfo;
        var accessFlags = field.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.name = field.getName();
        this.type = _findType(field.getGenericType(), classInfo);
        this.annotations = field.getDeclaredAnnotations();
        this.isFinal = accessFlags.contains(FINAL);
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
        field.setAccessible(flag);
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    /// 是否 final 字段
    public boolean isFinal() {
        return isFinal;
    }

}
