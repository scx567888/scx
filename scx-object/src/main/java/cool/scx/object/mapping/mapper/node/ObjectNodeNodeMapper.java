package cool.scx.object.mapping.mapper.node;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;

/// ObjectNodeNodeMapper
///
/// 不支持 单值数组解包, 和 MapNodeMapper 允许宽松处理不同
/// ObjectNode 作为中间表示层, 必须保证数据结构的准确性
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectNodeNodeMapper implements NodeMapper<ObjectNode> {

    @Override
    public Node toNode(ObjectNode value, ToNodeContext context) {
        return value.deepCopy();
    }

    @Override
    public ObjectNode fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ObjectNode 类型
        if (node instanceof ObjectNode objectNode) {
            return objectNode.deepCopy();
        }
        //3, 非 ObjectNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
