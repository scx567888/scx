package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;

/// NullNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class NullNodeNodeMapper implements NodeMapper<NullNode> {

    @Override
    public Node toNode(NullNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public NullNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return NullNode.NULL;
        }
        //2, 非 NullNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
