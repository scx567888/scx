package cool.scx.reflect.i;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cool.scx.reflect.i.ClassInfoHelper._findAccessModifier;
import static java.lang.reflect.AccessFlag.FINAL;

public class FieldInfoImpl implements FieldInfo {

    private final Field rawField;
    private final ClassInfo declaringClass;
    private final String name;
    private final AccessModifier accessModifier;
    private final boolean isFinal;
    private final Annotation[] annotations;
    private final ClassInfo classInfo;

    public FieldInfoImpl(Field field, ClassInfo declaringClass) {
        this.rawField = field;
        this.declaringClass = declaringClass;
        this.name = field.getName();
        var accessFlags = field.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.isFinal = accessFlags.contains(FINAL);
        this.annotations = field.getDeclaredAnnotations();
        this.classInfo = ScxReflect.getClassInfo(field.getGenericType());
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
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public ClassInfo classInfo() {
        return classInfo;
    }

}
