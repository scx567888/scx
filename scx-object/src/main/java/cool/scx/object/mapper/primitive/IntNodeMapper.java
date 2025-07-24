package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.IntNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// IntNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class IntNodeMapper implements NodeMapper<Integer> {

    private final boolean isPrimitive;

    public IntNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public Node toNode(Integer value, ToNodeContext context) {
        return new IntNode(value);
    }

    @Override
    public Integer fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null, 基本类型不允许 null
        if (node.isNull()) {
            return isPrimitive ? 0 : null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            try {
                return valueNode.asInt();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
