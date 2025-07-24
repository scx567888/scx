package cool.scx.object.mapper.math;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.BigIntegerNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

import java.math.BigInteger;

/// BigIntegerNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class BigIntegerNodeMapper implements NodeMapper<BigInteger> {

    @Override
    public Node toNode(BigInteger value, ToNodeContext context) {
        return new BigIntegerNode(value);
    }

    @Override
    public BigInteger fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理值类型
        if (node instanceof ValueNode valueNode) {
            try {
                return valueNode.asBigInteger();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
