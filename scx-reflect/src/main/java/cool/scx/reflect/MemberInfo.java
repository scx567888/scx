package cool.scx.reflect;

///  类成员接口
///
/// @author scx567888
/// @version 0.0.1
public interface MemberInfo extends AnnotatedElementInfo {

    ClassInfo classInfo();

    AccessModifier accessModifier();

    void setAccessible(boolean flag);

}
