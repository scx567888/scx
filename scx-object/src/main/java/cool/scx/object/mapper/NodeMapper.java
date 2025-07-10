package cool.scx.object.mapper;

import cool.scx.object.node.Node;

public interface NodeMapper<T> {
    
    /// 值 转换为 Node, value 永不为 null
    Node toNode(T value);
    
    /// Node 转换为 值, node 永不为 null
    T fromNode(Node node);
    
}
