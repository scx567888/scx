package cool.scx.object.mapper;

import cool.scx.object.node.Node;

//todo 待处理 这里需要支持 更细化的 Node 类型转换
public class NodeNodeMapper implements NodeMapper<Node> {

    @Override
    public Node toNode(Node value, NodeMapperSelector selector) {
        return value;
    }

    @Override
    public Node fromNode(Node node, NodeMapperSelector selector) {
        return node;
    }

}
