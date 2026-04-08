package cool.scx.object.mapping.mapper.primitive;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.TextNode;

/// CharNodeMapper
///
/// 支持 单值数组解包
///
/// @author scx567888
/// @version 0.0.1
public final class CharNodeMapper implements NodeMapper<Character> {

    private final boolean isPrimitive;

    public CharNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public Node toNode(Character value, ToNodeContext context) {
        return new TextNode(value.toString());
    }

    @Override
    public Character fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null, 基本类型不允许 null
        if (node == NullNode.NULL) {
            return isPrimitive ? (char) 0 : null;
        }
        //2, 只处理 TextNode 类型
        if (node instanceof TextNode textNode) {
            var text = textNode.asText();
            if (text.length() == 1) {
                return text.charAt(0);
            }
            throw new NodeMappingException("String length must be 1 for char, got: " + text);
        }
        //3, 尝试处理 单值数组 (这里假设 ArrayNode 不存在自引用)
        if (node instanceof ArrayNode arrayNode && arrayNode.size() == 1) {
            return this.fromNode(arrayNode.get(0), context);
        }
        //4, 非 TextNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
