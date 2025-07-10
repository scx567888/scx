package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.DoubleNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

public class DoubleNodeMapper extends PrimitiveNodeMapper<Double> {

    public DoubleNodeMapper(boolean isPrimitive) {
        super(isPrimitive, 0.0);
    }

    @Override
    public Node toNode(Double value, NodeMapperSelector selector) {
        return new DoubleNode(value);
    }

    @Override
    public Double fromNode0(Node node) {
        //1, 处理 null
        if (node == NullNode.NULL) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asDouble();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
