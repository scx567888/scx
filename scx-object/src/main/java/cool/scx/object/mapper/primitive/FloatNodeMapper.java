package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

public class FloatNodeMapper extends PrimitiveNodeMapper<Float> {

    public FloatNodeMapper(boolean isPrimitive) {
        super(isPrimitive, 0f);
    }

    @Override
    public Node toNode(Float value, NodeMapperSelector selector) {
        return new FloatNode(value);
    }

    @Override
    public Float fromNode0(Node node) {
        //1, 处理 null
        if (node == NullNode.NULL) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            return valueNode.asFloat();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
