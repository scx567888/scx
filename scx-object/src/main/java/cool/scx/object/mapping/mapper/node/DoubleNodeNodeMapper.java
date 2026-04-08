package cool.scx.object.mapping.mapper.node;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.DoubleNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// DoubleNodeNodeMapper
///
/// 不支持 单值数组解包, 和 DoubleNodeMapper 允许宽松处理不同
/// DoubleNode 作为中间表示层, 必须保证数据结构的准确性
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleNodeNodeMapper implements NodeMapper<DoubleNode> {

    @Override
    public Node toNode(DoubleNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public DoubleNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 DoubleNode 类型
        if (node instanceof DoubleNode doubleNode) {
            return doubleNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            try {
                return new DoubleNode(valueNode.asDouble());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
