package cool.scx.reflect;

/// ArrayTypeInfo
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ArrayTypeInfo extends TypeInfo permits ArrayTypeInfoImpl {

    /// 组件类型
    TypeInfo componentType();

}
