package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;

/// CharArrayNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class CharArrayNodeMapper implements NodeMapper<char[]> {

    @Override
    public Node toNode(char[] value, ToNodeContext context) {
        var arrayNode = new ArrayNode(value.length);
        for (var i : value) {
            arrayNode.add(new TextNode(String.valueOf(i)));
        }
        return arrayNode;
    }

    @Override
    public char[] fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理数组类型
        if (node instanceof ArrayNode arrayNode) {
            var result = new char[arrayNode.size()];
            var i = 0;
            for (var e : arrayNode) {
                result[i] = toChar(e);
                i = i + 1;
            }
            return result;
        }
        //3, 非数组类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private char toChar(Node element) throws NodeMappingException {
        //1, 不允许为 null
        if (element.isNull()) {
            throw new NodeMappingException("Element cannot be null");
        }
        //2, 只处理 TextNode 类型
        if (element instanceof TextNode textNode) {
            var text = textNode.asText();
            if (text.length() == 1) {
                return text.charAt(0);
            }
            throw new NodeMappingException("String length must be 1 for char, got: " + text);
        }
        //3, 非 TextNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + element.getClass());
    }

}
