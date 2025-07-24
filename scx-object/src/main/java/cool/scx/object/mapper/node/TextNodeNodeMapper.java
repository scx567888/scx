package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;
import cool.scx.object.node.ValueNode;

/// TextNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class TextNodeNodeMapper implements NodeMapper<TextNode> {

    @Override
    public Node toNode(TextNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public TextNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 TextNode 类型
        if (node instanceof TextNode textNode) {
            return textNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            return new TextNode(valueNode.asText());
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
