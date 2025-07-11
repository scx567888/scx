package cool.scx.object.mapper.array;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;

public class CharArrayNodeMapper implements NodeMapper<char[]> {

    @Override
    public Node toNode(char[] value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var i : value) {
            arrayNode.add(new TextNode(String.valueOf(i)));
        }
        return arrayNode;
    }

    @Override
    public char[] fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }
}
