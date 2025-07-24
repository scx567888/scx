package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;

/// ArrayNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayNodeNodeMapper implements NodeMapper<ArrayNode> {

    @Override
    public Node toNode(ArrayNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ArrayNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ArrayNode 类型
        if (node instanceof ArrayNode arrayNode) {
            return arrayNode.deepCopy();
        }
        //3, 非 ArrayNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
