package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;

import static cool.scx.reflect.ClassInfoHelper.*;
import static cool.scx.reflect.ReflectHelper._findAccessModifier;

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
    private final IConstructorInfo[] constructors;
    private final IConstructorInfo defaultConstructor;
    private final IConstructorInfo recordConstructor;
    private final IFieldInfo[] fields;
    private final IFieldInfo[] allFields;
    private final IMethodInfo[] methods;
    private final IMethodInfo[] allMethods;
    private final Annotation[] annotations;
    private final Annotation[] allAnnotations;
    private final boolean isFinal;
    private final boolean isStatic;
    private final boolean isAnonymousClass;
    private final boolean isMemberClass;
    private final boolean isPrimitive;
    private final boolean isArray;
    private final IClassInfo enumClass;
    private final IClassInfo componentType;

    public ClassInfo(JavaType type) {
        this.type = type;
        var rawClass = type.getRawClass();
        var accessFlags = rawClass.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.classType = _findClassType(rawClass, accessFlags);
        this.superClass = _findSuperClass(type);
        this.interfaces = _findInterfaces(type);
        this.constructors = _findConstructorInfos(this);
        this.defaultConstructor = _findDefaultConstructor(this);
        this.recordConstructor = _findRecordConstructor(this);
        this.fields = _findFieldInfos(this);
        this.allFields = _findAllFieldInfos(this);
        this.methods = _findMethodInfos(this);
        this.allMethods = _findAllMethodInfos(this);
        this.annotations = _findAnnotations(rawClass);
        this.allAnnotations = _findAllAnnotations(this);
        this.isFinal = _isFinal(accessFlags);
        this.isStatic = _isStatic(accessFlags);
        this.isAnonymousClass = _isAnonymousClass(rawClass);
        this.isMemberClass = _isMemberClass(rawClass);
        this.isPrimitive = _isPrimitive(rawClass);
        this.isArray = _isArray(rawClass);
        this.enumClass = _findEnumClass(this);
        this.componentType = _findComponentType(this);
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
    public IConstructorInfo[] constructors() {
        return constructors;
    }

    @Override
    public IConstructorInfo defaultConstructor() {
        return defaultConstructor;
    }

    @Override
    public IConstructorInfo recordConstructor() {
        return recordConstructor;
    }

    @Override
    public IFieldInfo[] fields() {
        return fields;
    }

    @Override
    public IFieldInfo[] allFields() {
        return allFields;
    }

    @Override
    public IMethodInfo[] methods() {
        return methods;
    }

    @Override
    public IMethodInfo[] allMethods() {
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
    public boolean isMemberClass() {
        return isMemberClass;
    }

    @Override
    public boolean isPrimitive() {
        return isPrimitive;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }


    @Override
    public IClassInfo enumClass() {
        return enumClass;
    }

    @Override
    public IClassInfo componentType() {
        return componentType;
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
