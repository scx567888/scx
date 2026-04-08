package cool.scx.object.mapping.mapper.node;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// FloatNodeNodeMapper
///
/// 不支持 单值数组解包, 和 FloatNodeMapper 允许宽松处理不同
/// FloatNode 作为中间表示层, 必须保证数据结构的准确性
///
/// @author scx567888
/// @version 0.0.1
public final class FloatNodeNodeMapper implements NodeMapper<FloatNode> {

    @Override
    public Node toNode(FloatNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public FloatNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 FloatNode 类型
        if (node instanceof FloatNode intNode) {
            return intNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            try {
                return new FloatNode(valueNode.asFloat());
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
