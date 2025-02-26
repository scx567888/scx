package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;

/// 仅用作规范实现
public interface IClassInfo {

    /// Java Type 这里我们使用 Jackson 的 JavaType 来方便进行诸如序列化等操作
    JavaType type();

    /// 类修饰符
    AccessModifier accessModifier();

    /// 类的类型
    ClassType classType();

    /// 父类 可能为空
    IClassInfo superClass();

    /// 接口
    IClassInfo[] interfaces();

    /// 构造参数
    IConstructorInfo[] constructors();

    /// 默认构造函数 (无参构造函数) 可能为空
    IConstructorInfo defaultConstructor();

    /// Record 规范构造参数 可能为空
    IConstructorInfo recordConstructor();

    /// 字段
    IFieldInfo[] fields();

    /// 获取类所有字段 包括继承自父类的字段
    IFieldInfo[] allFields();

    /// 方法
    IMethodInfo[] methods();

    /// 获取类所有方法 包括继承自父类的方法
    IMethodInfo[] allMethods();

    /// 注解
    Annotation[] annotations();

    /// 获取类所有的注解 包括继承自父类的注解
    Annotation[] allAnnotations();

    /// 是否 final 类
    boolean isFinal();

    /// 是否 静态类
    boolean isStatic();

    /// 是否 匿名类
    boolean isAnonymousClass();

    /// 是否 内部类
    boolean isMemberClass();

    /// 是否基本类型
    boolean isPrimitive();

    /// 是否数组
    boolean isArray();

    /// 枚举类型
    IClassInfo enumClass();

    /// 数组成员类型
    IClassInfo componentType();

    /// 返回指定类型的 父级 ClassInfo 支持常规类,抽象类,接口
    IClassInfo findSuperType(Class<?> type);

}
