package cool.scx.reflect;

public interface MemberInfo extends AnnotatedElementInfo {

    /// 持有当前 成员的 ClassInfo
    ClassInfo declaringClass();

    /// 访问修饰符
    AccessModifier accessModifier();

    /// 允许方法
    void setAccessible(boolean flag);

}
