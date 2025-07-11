package cool.scx.object.mapper;

import cool.scx.object.node.Node;

//todo 待处理
public class NodeNodeMapper implements NodeMapper<Node> {

    @Override
    public Node toNode(Node value, NodeMapperSelector selector) {
        return value;
    }

    @Override
    public Node fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }
    
}
