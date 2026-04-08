package cool.scx.object.mapping;

import cool.scx.object.node.Node;

/// Object -> Node 上下文
public interface ToNodeContext {

    /// @param value       待转换的值
    /// @param pathSegment 路径段 (一般用于调试)
    Node toNode(Object value, Object pathSegment) throws NodeMappingException;

    /// 配置项
    ToNodeOptions options();

}
