package cool.scx.object.mapper.array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.DoubleNode;
import cool.scx.object.node.Node;

public class DoubleArrayNodeMapper implements NodeMapper<double[]> {

    @Override
    public Node toNode(double[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var i : value) {
            arrayNode.add(new DoubleNode(i));
        }
        return arrayNode;
    }

    @Override
    public double[] fromNode(Node node) {
        return null;
    }

}
