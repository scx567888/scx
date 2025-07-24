package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.LongNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// LongArrayNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class LongArrayNodeMapper implements NodeMapper<long[]> {

    @Override
    public Node toNode(long[] value, ToNodeContext context) {
        var arrayNode = new ArrayNode(value.length);
        for (var i : value) {
            arrayNode.add(new LongNode(i));
        }
        return arrayNode;
    }

    @Override
    public long[] fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理数组类型
        if (node instanceof ArrayNode arrayNode) {
            var result = new long[arrayNode.size()];
            var i = 0;
            for (var e : arrayNode) {
                result[i] = toLong(e);
                i = i + 1;
            }
            return result;
        }
        //3, 非数组类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private long toLong(Node element) throws NodeMappingException {
        //1, 不允许为 null
        if (element.isNull()) {
            throw new NodeMappingException("Element cannot be null");
        }
        //2, 只处理值类型
        if (element instanceof ValueNode valueNode) {
            try {
                return valueNode.asLong();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + element.getClass());
    }

}
