package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// FloatNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class FloatNodeMapper implements NodeMapper<Float> {

    private final boolean isPrimitive;

    public FloatNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public Node toNode(Float value, ToNodeContext context) {
        return new FloatNode(value);
    }

    @Override
    public Float fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null, 基本类型不允许 null
        if (node.isNull()) {
            return isPrimitive ? 0f : null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            try {
                return valueNode.asFloat();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
