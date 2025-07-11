package cool.scx.object.mapper.array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.IntNode;
import cool.scx.object.node.Node;

public class IntArrayNodeMapper implements NodeMapper<int[]> {

    @Override
    public Node toNode(int[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (int i : value) {
            arrayNode.add(new IntNode(i));
        }
        return arrayNode;
    }

    @Override
    public int[] fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }

}
