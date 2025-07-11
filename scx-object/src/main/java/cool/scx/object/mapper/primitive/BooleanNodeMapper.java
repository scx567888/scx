package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

import static cool.scx.object.node.BooleanNode.FALSE;
import static cool.scx.object.node.BooleanNode.TRUE;

public class BooleanNodeMapper extends PrimitiveNodeMapper<Boolean> {

    public BooleanNodeMapper(boolean isPrimitive) {
        super(isPrimitive, false);
    }

    @Override
    public Node toNode(Boolean value, NodeMapperSelector selector) {
        return value ? TRUE : FALSE;
    }

    @Override
    public Boolean fromNode0(Node node) {
        //1, 处理 null
        if (node == NullNode.NULL) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asBoolean();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
