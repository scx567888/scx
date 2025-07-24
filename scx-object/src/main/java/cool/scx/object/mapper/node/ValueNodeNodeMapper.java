package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// ValueNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ValueNodeNodeMapper implements NodeMapper<ValueNode> {

    @Override
    public Node toNode(ValueNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ValueNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ValueNode 类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.deepCopy();
        }
        //3, 非 ValueNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
