package cool.scx.reflect.i;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.i.ReflectHelper._findAccessModifier;
import static java.lang.reflect.AccessFlag.FINAL;
import static java.lang.reflect.AccessFlag.STATIC;

public final class FieldInfoImpl implements FieldInfo {

    private final Field rawField;
    private final ClassInfo declaringClass;
    private final String name;
    private final AccessModifier accessModifier;
    private final boolean isFinal;
    private final boolean isStatic;
    private final ClassInfo fieldType;
    private final Annotation[] annotations;

    public FieldInfoImpl(Field field, ClassInfo declaringClass) {
        this.rawField = field;
        this.declaringClass = declaringClass;
        this.name = field.getName();
        var accessFlags = field.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.isFinal = accessFlags.contains(FINAL);
        this.isStatic = accessFlags.contains(STATIC);
        this.fieldType = ScxReflect.getClassInfo(field.getGenericType());
        this.annotations = field.getDeclaredAnnotations();
    }

    @Override
    public Field rawField() {
        return rawField;
    }

    @Override
    public ClassInfo declaringClass() {
        return declaringClass;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
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
    public ClassInfo fieldType() {
        return fieldType;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

}
