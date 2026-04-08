package cool.scx.object.mapping.mapper.node;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.Node;

/// NodeNodeMapper
///
/// 直接 deepCopy
///
/// @author scx567888
/// @version 0.0.1
public final class NodeNodeMapper implements NodeMapper<Node> {

    @Override
    public Node toNode(Node value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public Node fromNode(Node node, FromNodeContext context) {
        return node.deepCopy();
    }

}
