package cool.scx.object.mapping.mapper.math;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.BigDecimalNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

import java.math.BigDecimal;

/// BigDecimalNodeMapper
///
/// 支持 单值数组解包
///
/// @author scx567888
/// @version 0.0.1
public final class BigDecimalNodeMapper implements NodeMapper<BigDecimal> {

    @Override
    public Node toNode(BigDecimal value, ToNodeContext context) {
        return new BigDecimalNode(value);
    }

    @Override
    public BigDecimal fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            try {
                return valueNode.asBigDecimal();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 尝试处理 单值数组 (这里假设 ArrayNode 不存在自引用)
        if (node instanceof ArrayNode arrayNode && arrayNode.size() == 1) {
            return this.fromNode(arrayNode.get(0), context);
        }
        //4, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
