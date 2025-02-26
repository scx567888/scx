package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;

import static cool.scx.reflect.ClassInfoHelper.*;
import static cool.scx.reflect.ReflectHelper._findAccessModifier;

/// ClassInfo
///
/// @author scx567888
/// @version 0.0.1
public final class ClassInfo  {

    private final JavaType type;
    private final AccessModifier accessModifier;
    private final ClassType classType;
    private final ClassInfo superClass;
    private final ClassInfo[] interfaces;
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
    private final ClassInfo enumClass;
    private final ClassInfo componentType;

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

    /// Java Type 这里我们使用 Jackson 的 JavaType 来方便进行诸如序列化等操作
    public JavaType type() {
        return type;
    }

    /// 类修饰符
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    /// 类的类型
    public ClassType classType() {
        return classType;
    }

    /// 父类 可能为空
    public ClassInfo superClass() {
        return superClass;
    }

    /// 接口
    public ClassInfo[] interfaces() {
        return interfaces;
    }

    /// 构造参数
    public IConstructorInfo[] constructors() {
        return constructors;
    }

    /// 默认构造函数 (无参构造函数) 可能为空
    public IConstructorInfo defaultConstructor() {
        return defaultConstructor;
    }

    /// Record 规范构造参数 可能为空
    public IConstructorInfo recordConstructor() {
        return recordConstructor;
    }

    /// 字段
    public IFieldInfo[] fields() {
        return fields;
    }

    /// 获取类所有字段 包括继承自父类的字段
    public IFieldInfo[] allFields() {
        return allFields;
    }

    /// 方法
    public IMethodInfo[] methods() {
        return methods;
    }

    /// 获取类所有方法 包括继承自父类的方法
    public IMethodInfo[] allMethods() {
        return allMethods;
    }

    /// 注解
    public Annotation[] annotations() {
        return annotations;
    }

    /// 获取类所有的注解 包括继承自父类的注解
    public Annotation[] allAnnotations() {
        return allAnnotations;
    }

    /// 是否 final 类
    public boolean isFinal() {
        return isFinal;
    }

    /// 是否 静态类
    public boolean isStatic() {
        return isStatic;
    }

    /// 是否 匿名类
    public boolean isAnonymousClass() {
        return isAnonymousClass;
    }

    /// 是否 内部类
    public boolean isMemberClass() {
        return isMemberClass;
    }

    /// 是否基本类型
    public boolean isPrimitive() {
        return isPrimitive;
    }

    /// 是否数组
    public boolean isArray() {
        return isArray;
    }

    /// 枚举类型
    public ClassInfo enumClass() {
        return enumClass;
    }

    /// 数组成员类型
    public ClassInfo componentType() {
        return componentType;
    }

    /// 返回指定类型的 父级 ClassInfo 支持常规类,抽象类,接口
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
