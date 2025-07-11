package cool.scx.object.mapper.array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;

public class FloatArrayNodeMapper implements NodeMapper<float[]> {

    @Override
    public Node toNode(float[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var i : value) {
            arrayNode.add(new FloatNode(i));
        }
        return arrayNode;
    }

    @Override
    public float[] fromNode(Node node) {
        return null;
    }

}
