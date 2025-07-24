package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.BooleanNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// BooleanNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanNodeMapper implements NodeMapper<Boolean> {

    private final boolean isPrimitive;

    public BooleanNodeMapper(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public Node toNode(Boolean value, ToNodeContext context) {
        return BooleanNode.of(value);
    }

    @Override
    public Boolean fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null, 基本类型不允许 null
        if (node.isNull()) {
            return isPrimitive ? false : null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asBoolean();
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
