package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.IntNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;

//todo 待优化
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
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ArrayNode arrayNode) {
            var array = new int[arrayNode.size()];
            int i = 0;
            for (var n : arrayNode) {
                var e = selector.fromNode(n, int.class);
                array[i] = e;
                i = i + 1;
            }
            return array;
        }
        throw new IllegalArgumentException("必须是数组");
    }

}
