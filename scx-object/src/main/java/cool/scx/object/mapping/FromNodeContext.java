package cool.scx.object.mapping;

import cool.scx.object.node.Node;
import cool.scx.reflect.TypeInfo;

/// Node -> Object 上下文
public interface FromNodeContext {

    /// @param node Node
    /// @param type 类型
    <T> T fromNode(Node node, TypeInfo type) throws NodeMappingException;

    /// 配置项
    FromNodeOptions options();

}
