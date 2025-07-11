package cool.scx.object.mapper.math;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.BigIntegerNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

import java.math.BigInteger;

public class BigIntegerNodeMapper implements NodeMapper<BigInteger> {

    @Override
    public Node toNode(BigInteger value, NodeMapperSelector selector) {
        return new BigIntegerNode(value);
    }

    @Override
    public BigInteger fromNode(Node node, NodeMapperSelector selector) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ValueNode valueNode) {
            return valueNode.asBigInteger();
        }
        throw new IllegalArgumentException("不支持的类型");
    }

}
