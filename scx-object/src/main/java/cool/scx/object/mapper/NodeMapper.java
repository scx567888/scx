package cool.scx.object.mapper;

import cool.scx.object.node.Node;

/// 值 和 Node 的双向映射器
public interface NodeMapper<T> {

    /// 将 值 转换为 Node.
    ///
    /// @param value    永不为 null
    /// @param selector 可用于递归调用
    /// @return Node
    Node toNode(T value, NodeMapperSelector selector);

    /// 将 Node 转换为 值.
    ///
    /// @param node     永不为 null, 但可以是 NullNode.NULL
    /// @param selector 可用于递归调用
    /// @return 值
    T fromNode(Node node, NodeMapperSelector selector);

}
