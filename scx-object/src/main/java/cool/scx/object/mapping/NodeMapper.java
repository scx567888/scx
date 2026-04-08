package cool.scx.object.mapping;

import cool.scx.object.node.Node;

/// 值 和 Node 的双向映射器
///
/// @author scx567888
/// @version 0.0.1
public interface NodeMapper<T> {

    /// 将 值 转换为 Node.
    ///
    /// @param value   永不为 null
    /// @param context 映射上下文, 可用于递归或动态配置
    /// @return Node
    Node toNode(T value, ToNodeContext context) throws NodeMappingException;

    /// 将 Node 转换为 值.
    ///
    /// @param node    永不为 null, 但可以是 NullNode.NULL
    /// @param context 映射上下文, 可用于递归或动态配置
    /// @return 值
    T fromNode(Node node, FromNodeContext context) throws NodeMappingException;

}
