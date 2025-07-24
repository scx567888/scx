package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.BigIntegerNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// BigIntegerNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BigIntegerNodeNodeMapper implements NodeMapper<BigIntegerNode> {

    @Override
    public Node toNode(BigIntegerNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BigIntegerNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 BigIntegerNode 类型
        if (node instanceof BigIntegerNode bigIntegerNode) {
            return bigIntegerNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            try {
                return new BigIntegerNode(valueNode.asBigInteger());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
