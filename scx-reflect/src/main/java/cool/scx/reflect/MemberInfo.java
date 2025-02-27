package cool.scx.reflect;

///  类成员接口
public interface MemberInfo extends AnnotatedElementInfo {

    ClassInfo classInfo();

    AccessModifier accessModifier();

    void setAccessible(boolean flag);

}
