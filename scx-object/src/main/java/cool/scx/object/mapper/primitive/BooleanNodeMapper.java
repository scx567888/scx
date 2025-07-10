package cool.scx.object.mapper.primitive;

import cool.scx.object.node.BooleanNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

public class BooleanNodeMapper extends PrimitiveNodeMapper<Boolean> {

    public BooleanNodeMapper(boolean isPrimitive) {
        super(isPrimitive, false);
    }

    @Override
    public Node toNode(Boolean value) {
        return value ? BooleanNode.TRUE : BooleanNode.FALSE;
    }

    @Override
    public Boolean fromNode0(Node node) {
        //1, 处理 null
        if (node== NullNode.NULL){
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode){
            return valueNode.asBoolean();
        }
        //3, 非值类型无法转换直接报错 
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
