package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.IntNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// IntNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class IntNodeNodeMapper implements NodeMapper<IntNode> {

    @Override
    public Node toNode(IntNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public IntNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 IntNode 类型
        if (node instanceof IntNode intNode) {
            return intNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            try {
                return new IntNode(valueNode.asInt());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
