package cool.scx.object.mapper.node;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.BigDecimalNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// BigDecimalNodeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BigDecimalNodeNodeMapper implements NodeMapper<BigDecimalNode> {

    @Override
    public Node toNode(BigDecimalNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BigDecimalNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 BigDecimalNode 类型
        if (node instanceof BigDecimalNode bigDecimalNode) {
            return bigDecimalNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            try {
                return new BigDecimalNode(valueNode.asBigDecimal());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
