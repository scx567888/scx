package cool.scx.object.mapper.string;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;
import cool.scx.object.node.ValueNode;

/// StringNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class StringNodeMapper implements NodeMapper<String> {

    @Override
    public Node toNode(String value, ToNodeContext context) {
        return new TextNode(value);
    }

    @Override
    public String fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asText();
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
