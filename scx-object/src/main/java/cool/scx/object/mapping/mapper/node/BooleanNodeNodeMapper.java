package cool.scx.object.mapping.mapper.node;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.BooleanNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// BooleanNodeNodeMapper
///
/// 不支持 单值数组解包, 和 BooleanNodeMapper 允许宽松处理不同
/// BooleanNode 作为中间表示层, 必须保证数据结构的准确性
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanNodeNodeMapper implements NodeMapper<BooleanNode> {

    @Override
    public Node toNode(BooleanNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public BooleanNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 处理 BooleanNode 类型
        if (node instanceof BooleanNode booleanNode) {
            return booleanNode.deepCopy();
        }
        //3, 尝试转换
        if (node instanceof ValueNode valueNode) {
            return BooleanNode.of(valueNode.asBoolean());
        }
        //4, 其余类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
