package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;

/// NodeNodeMapper
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
