package cool.scx.object.mapper.math;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.BigDecimalNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ValueNode;

import java.math.BigDecimal;

public class BigDecimalNodeMapper implements NodeMapper<BigDecimal> {

    @Override
    public Node toNode(BigDecimal value, NodeMapperSelector selector) {
        return new BigDecimalNode(value);
    }

    @Override
    public BigDecimal fromNode(Node node, NodeMapperSelector selector) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ValueNode valueNode) {
            return valueNode.asBigDecimal();
        }
        throw new IllegalArgumentException("不支持的类型");
    }

}
