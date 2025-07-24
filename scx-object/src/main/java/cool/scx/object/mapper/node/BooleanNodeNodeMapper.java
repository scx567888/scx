package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.BooleanNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// BooleanNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanNodeNodeMapper implements NodeMapper<BooleanNode> {

    @Override
    public Node toNode(BooleanNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BooleanNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 BooleanNode 类型
        if (node instanceof BooleanNode booleanNode) {
            return booleanNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            return BooleanNode.of(valueNode.asBoolean());
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
