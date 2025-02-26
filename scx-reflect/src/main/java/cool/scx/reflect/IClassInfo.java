package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
    ConstructorInfo[] constructors();

    /// 默认构造函数 (无参构造函数) 可能为空
    ConstructorInfo defaultConstructor();

    /// Record 规范构造参数 可能为空
    ConstructorInfo recordConstructor();

    /// 字段
    FieldInfo[] fields();

    /// 获取类所有字段 包括继承自父类的字段
    FieldInfo[] allFields();

    /// 方法
    MethodInfo[] methods();

    /// 获取类所有方法 包括继承自父类的方法
    MethodInfo[] allMethods();

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

    /// 泛型
    Type[] genericTypes();

    /// 枚举类型
    IClassInfo enumClass();

    /// 返回指定的 type
    IClassInfo findSuperType(Class<?> type);

}
