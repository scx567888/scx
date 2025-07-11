package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;

import static cool.scx.object.node.BooleanNode.FALSE;
import static cool.scx.object.node.BooleanNode.TRUE;

//todo 待优化
public class BooleanArrayNodeMapper implements NodeMapper<boolean[]> {

    @Override
    public Node toNode(boolean[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var i : value) {
            arrayNode.add(i ? TRUE : FALSE);
        }
        return arrayNode;
    }

    @Override
    public boolean[] fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }

}
