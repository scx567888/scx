package cool.scx.reflect;

import java.lang.reflect.TypeVariable;
import java.util.Map;

/// ClassInfo
///
/// 在当前环境中 可以看作 「Class + 类型参数」.
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ClassInfo extends TypeInfo, AccessModifierOwner, AnnotatedElementInfo permits ClassInfoImpl {

    //***************** TypeInfo **************

    /// 原始类
    Class<?> rawClass();

    /// 泛型绑定
    Map<TypeVariable<?>, TypeInfo> bindings();

    //************ 类的基本信息 ***************

    /// 类名
    String name();

    /// 类的类型
    ClassKind classKind();

    //************ 类属性 *****************

    boolean isAbstract();

    /// 是否 final 类
    boolean isFinal();

    /// 是否 静态类
    boolean isStatic();

    /// 是否 匿名类
    boolean isAnonymousClass();

    /// 是否 内部类
    boolean isMemberClass();

    //************* 继承结构 *****************

    /// 父类 可能为空
    ClassInfo superClass();

    /// 接口列表
    ClassInfo[] interfaces();

    //************** 类成员 ********************

    /// 构造参数列表
    ConstructorInfo[] constructors();

    /// 字段列表
    FieldInfo[] fields();

    /// 方法列表
    MethodInfo[] methods();

    //************* 辅助方法 **************

    /// 默认构造函数 (无参构造函数) 可能为空
    ConstructorInfo defaultConstructor();

    /// Record 规范构造参数 可能为空
    ConstructorInfo recordConstructor();

    /// 获取类所有字段 包括继承自父类的字段
    FieldInfo[] allFields();

    /// 获取类所有方法 包括继承自父类的方法
    MethodInfo[] allMethods();

    /// 枚举类型 (如果类是匿名枚举类的话可以正确获取到真正的枚举类型)
    ClassInfo enumClass();

    /// 返回指定类型的 父级 ClassInfo 支持常规类,抽象类,接口
    default ClassInfo findSuperType(Class<?> rawTarget) {
        if (rawTarget == this.rawClass()) {
            return this;
        }
        // Check super interfaces first:
        if (rawTarget.isInterface()) {
            for (var anInterface : interfaces()) {
                var type = anInterface.findSuperType(rawTarget);
                if (type != null) {
                    return type;
                }
            }
        }
        // and if not found, super class and its supertypes
        if (superClass() != null) {
            return superClass().findSuperType(rawTarget);
        }
        return null;
    }

}
