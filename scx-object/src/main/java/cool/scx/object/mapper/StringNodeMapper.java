package cool.scx.object.mapper;

import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.TextNode;
import cool.scx.object.node.ValueNode;

public class StringNodeMapper implements NodeMapper<String> {

    @Override
    public Node toNode(String value, NodeMapperSelector selector) {
        return new TextNode(value);
    }

    @Override
    public String fromNode(Node node) {
        //1, 处理 null
        if (node == NullNode.NULL) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asText();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
