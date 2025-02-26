package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static cool.scx.reflect.ClassInfoHelper.*;

/// ClassInfo
///
/// @author scx567888
/// @version 0.0.1
public final class ClassInfo implements IClassInfo {

    private final JavaType type;
    private final AccessModifier accessModifier;
    private final ClassType classType;
    private final IClassInfo superClass;
    private final IClassInfo[] interfaces;
    private final ConstructorInfo[] constructors;
    private final ConstructorInfo defaultConstructor;
    private final ConstructorInfo recordConstructor;
    private final FieldInfo[] fields;
    private final FieldInfo[] allFields;
    private final MethodInfo[] methods;
    private final MethodInfo[] allMethods;
    private final Annotation[] annotations;
    private final Annotation[] allAnnotations;
    private final Type[] genericTypes;
    private final IClassInfo enumClass;
    private final boolean isFinal;
    private final boolean isStatic;
    private final boolean isAnonymousClass;
    private final boolean isInnerClass;

    public ClassInfo(JavaType type) {
        this.type = type;
        this.accessModifier = _findAccessModifier(this);
        this.classType = _findClassType(this);
        this.superClass = _findSuperClass(this);
        this.interfaces = _findInterfaces(this);
        this.constructors = _findConstructorInfos(this);
        this.defaultConstructor = _findNoArgsConstructor(this);
        this.recordConstructor = _findRecordConstructor(this);
        this.fields = _findFieldInfos(this);
        this.allFields = _findAllFieldInfos(this);
        this.methods = _findMethodInfos(this);
        this.allMethods = _findAllMethodInfos(this);
        this.annotations = _findAnnotations(this);
        this.allAnnotations = _findAllAnnotations(this);
        this.genericTypes = null; //
        this.enumClass = _findEnumClass(this);
        this.isFinal = _findIsFinal(this);
        this.isStatic = _findIsStatic(this);
        this.isAnonymousClass = _findIsAnonymousClass(this);
        this.isInnerClass = _findIsInnerClass(this);
    }

    @Override
    public JavaType type() {
        return type;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public ClassType classType() {
        return classType;
    }

    @Override
    public IClassInfo superClass() {
        return superClass;
    }

    @Override
    public IClassInfo[] interfaces() {
        return interfaces;
    }

    @Override
    public ConstructorInfo[] constructors() {
        return constructors;
    }

    @Override
    public ConstructorInfo defaultConstructor() {
        return defaultConstructor;
    }

    @Override
    public ConstructorInfo recordConstructor() {
        return recordConstructor;
    }

    @Override
    public FieldInfo[] fields() {
        return fields;
    }

    @Override
    public FieldInfo[] allFields() {
        return allFields;
    }

    @Override
    public MethodInfo[] methods() {
        return methods;
    }

    @Override
    public MethodInfo[] allMethods() {
        return allMethods;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    @Override
    public Type[] genericTypes() {
        return genericTypes;
    }

    @Override
    public IClassInfo enumClass() {
        return enumClass;
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
    public boolean isAnonymousClass() {
        return isAnonymousClass;
    }

    @Override
    public boolean isInnerClass() {
        return isInnerClass;
    }

    @Override
    public IClassInfo findSuperType(Class<?> rawTarget) {
        if (rawTarget == type.getRawClass()) {
            return this;
        }
        // Check super interfaces first:
        if (rawTarget.isInterface()) {
            for (var anInterface : interfaces) {
                var type = anInterface.findSuperType(rawTarget);
                if (type != null) {
                    return type;
                }
            }
        }
        // and if not found, super class and its supertypes
        if (superClass != null) {
            return superClass.findSuperType(rawTarget);
        }
        return null;
    }

}
