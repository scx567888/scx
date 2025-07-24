package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.*;

/// NumericNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class NumericNodeNodeMapper implements NodeMapper<NumberNode> {

    @Override
    public Node toNode(NumberNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public NumberNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 NumberNode 类型
        if (node instanceof NumberNode numberNode) {
            return numberNode.deepCopy();
        }
        //3, 处理 BooleanNode 类型
        if (node instanceof BooleanNode booleanNode) {
            return new IntNode(booleanNode.asInt());
        }
        //4, 尝试转换 TextNode 类型
        if (node instanceof TextNode textNode) {
            // 我们不知道 TextNode 到底能转成什么, 这里假设是 int
            try {
                return new IntNode(textNode.asInt());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
