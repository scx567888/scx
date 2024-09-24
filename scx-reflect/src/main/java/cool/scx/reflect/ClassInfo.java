package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;

import static cool.scx.reflect.ReflectFactory.*;

/**
 * ClassInfo
 */
public final class ClassInfo {

    private final JavaType type;
    private final ClassInfo superClass;
    private final ClassInfo[] interfaces;
    private final boolean isRecord;
    private final boolean isInterface;
    private final boolean isAbstract;
    private final boolean isEnum;
    private final boolean isAnonymousClass;
    private final Annotation[] annotations;
    private final ConstructorInfo[] constructors;
    private final FieldInfo[] fields;
    private final MethodInfo[] methods;
    private final Annotation[] allAnnotations;
    private final FieldInfo[] allFields;
    private final MethodInfo[] allMethods;
    private final ConstructorInfo noArgsConstructor;
    private final ConstructorInfo recordConstructor;
    private final ClassInfo enumClass;

    ClassInfo(JavaType type) {
        this.type = type;
        this.superClass = _findSuperClass(this);
        this.interfaces = _findInterfaces(this);
        this.isRecord = _isRecord(this);
        this.isInterface = _isInterface(this);
        this.isAbstract = _isAbstract(this);
        this.isEnum = _isEnum(this);
        this.isAnonymousClass = _isAnonymousClass(this);
        this.annotations = _findAnnotations(this);
        this.constructors = _findConstructorInfos(this);
        this.fields = _findFieldInfos(this);
        this.methods = _findMethodInfos(this);
        this.allAnnotations = _findAllAnnotations(this);
        this.allFields = _findAllFieldInfos(this);
        this.allMethods = _findAllMethodInfos(this);
        this.noArgsConstructor = _findNoArgsConstructor(this);
        this.recordConstructor = _findRecordConstructor(this);
        this.enumClass = _findEnumClass(this);
    }

    public JavaType type() {
        return type;
    }

    public ClassInfo superClass() {
        return superClass;
    }

    public ClassInfo[] interfaces() {
        return interfaces;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public boolean isAnonymousClass() {
        return isAnonymousClass;
    }

    public Annotation[] annotations() {
        return annotations;
    }

    public ConstructorInfo[] constructors() {
        return constructors;
    }

    public FieldInfo[] fields() {
        return fields;
    }

    public MethodInfo[] methods() {
        return methods;
    }

    /**
     * 获取类所有的注解 包括继承自父类的注解
     *
     * @return 所有注解
     */
    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    /**
     * 获取类所有字段 包括继承自父类的字段
     *
     * @return 所有字段
     */
    public FieldInfo[] allFields() {
        return allFields;
    }

    /**
     * 获取类所有方法 包括继承自父类的方法
     *
     * @return 所有方法
     */
    public MethodInfo[] allMethods() {
        return allMethods;
    }

    /**
     * 无参构造函数
     */
    public ConstructorInfo noArgsConstructor() {
        return noArgsConstructor;
    }

    /**
     * Record 规范构造参数
     */
    public ConstructorInfo recordConstructor() {
        return recordConstructor;
    }

    public ClassInfo enumClass() {
        return enumClass;
    }

    public ClassInfo findSuperType(Class<?> rawTarget) {
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
