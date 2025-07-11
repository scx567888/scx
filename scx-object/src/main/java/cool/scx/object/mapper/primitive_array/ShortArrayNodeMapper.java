package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.IntNode;
import cool.scx.object.node.Node;

//todo 待优化
public class ShortArrayNodeMapper implements NodeMapper<short[]> {

    @Override
    public Node toNode(short[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (int i : value) {
            arrayNode.add(new IntNode(i));
        }
        return arrayNode;
    }

    @Override
    public short[] fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }

}
