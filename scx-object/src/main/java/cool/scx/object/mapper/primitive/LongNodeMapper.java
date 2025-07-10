package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.LongNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

public class LongNodeMapper extends PrimitiveNodeMapper<Long> {

    public LongNodeMapper(boolean isPrimitive) {
        super(isPrimitive, 0L);
    }

    @Override
    public Node toNode(Long value, NodeMapperSelector selector) {
        return new LongNode(value);
    }

    @Override
    public Long fromNode0(Node node) {
        //1, 处理 null
        if (node == NullNode.NULL) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asLong();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
