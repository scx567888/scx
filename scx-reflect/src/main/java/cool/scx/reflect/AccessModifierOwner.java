package cool.scx.reflect;

/// 具有访问修饰符的元素
public sealed interface AccessModifierOwner permits AccessModifierController, ClassInfo {
    
    AccessModifier accessModifier();

}
