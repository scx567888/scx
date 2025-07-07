package cool.scx.reflect;

/// 能够控制 访问的
public sealed interface AccessModifierController extends AccessModifierOwner permits MemberInfo {

    /// 允许访问
    void setAccessible(boolean flag);

    /// 是否允许访问
    boolean isAccessible();

}
