package cool.scx.reflect.impl;

import cool.scx.reflect.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import static cool.scx.reflect.impl.ReflectHelper.*;
import static cool.scx.reflect.impl.TypeFactory.TYPE_CACHE;
import static java.lang.reflect.AccessFlag.*;

/// ClassInfoImpl
///
/// @author scx567888
/// @version 0.0.1
public final class ClassInfoImpl implements ClassInfo {

    // TypeInfo
    private final Class<?> rawClass;
    private final Map<TypeVariable<?>, TypeInfo> bindings;

    // 类的基本信息
    private final String name;
    private final AccessModifier accessModifier;
    private final ClassKind classKind;

    // 类属性
    private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean isStatic;
    private final boolean isAnonymousClass;
    private final boolean isMemberClass;

    // 继承结构
    private final ClassInfo superClass;
    private final ClassInfo[] interfaces;

    // 类成员
    private final ConstructorInfo[] constructors;
    private final ConstructorInfo defaultConstructor;
    private final ConstructorInfo recordConstructor;

    private final FieldInfo[] fields;
    private final FieldInfo[] allFields;

    private final MethodInfo[] methods;
    private final MethodInfo[] allMethods;

    // 注解
    private final Annotation[] annotations;


    ClassInfoImpl(Type type, Map<TypeVariable<?>, TypeInfo> bindings) {
        TYPE_CACHE.put(type, this);
        this.rawClass = _findRawClass(type);
        this.bindings = _findBindings(type, bindings);

        this.name = this.rawClass.getName();

        var accessFlags = this.rawClass.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.classKind = _findClassKind(this.rawClass, accessFlags);
        this.isAbstract = accessFlags.contains(ABSTRACT);
        this.isFinal = accessFlags.contains(FINAL);
        this.isStatic = accessFlags.contains(STATIC);
        this.isAnonymousClass = this.rawClass.isAnonymousClass();
        this.isMemberClass = this.rawClass.isMemberClass();

        this.superClass = _findSuperClass(this.rawClass, this.bindings);
        this.interfaces = _findInterfaces(this.rawClass, this.bindings);

        this.constructors = _findConstructors(this.rawClass, this);
        this.defaultConstructor = _findDefaultConstructor(this.constructors);
        this.recordConstructor = _findRecordConstructor(this);

        this.fields = _findFields(this.rawClass, this);
        this.allFields = _findAllFields(this);

        this.methods = _findMethods(this.rawClass, this);
        this.allMethods = _findAllMethods(this);

        this.annotations = this.rawClass.getDeclaredAnnotations();

    }

    @Override
    public Class<?> rawClass() {
        return rawClass;
    }

    @Override
    public Map<TypeVariable<?>, TypeInfo> bindings() {
        return bindings;
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
    public ClassKind classKind() {
        return classKind;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
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
    public ClassInfo superClass() {
        return superClass;
    }

    @Override
    public ClassInfo[] interfaces() {
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
    public String toString() {
        var shortName = rawClass.getSimpleName();
        var typeArgs = bindings.values().stream().map(TypeInfo::toString).toList();
        if (typeArgs.isEmpty()) {
            return shortName;
        } else {
            return shortName + "<" + String.join(", ", typeArgs) + ">";
        }
    }

}
