package cool.scx.object.mapper.array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.LongNode;
import cool.scx.object.node.Node;

public class LongArrayNodeMapper implements NodeMapper<long[]> {

    @Override
    public Node toNode(long[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var i : value) {
            arrayNode.add(new LongNode(i));
        }
        return arrayNode;
    }

    @Override
    public long[] fromNode(Node node) {
        return null;
    }

}
