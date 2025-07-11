package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;

//todo 待优化
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
    public float[] fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }

}
