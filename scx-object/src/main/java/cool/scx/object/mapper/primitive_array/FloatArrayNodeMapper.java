package cool.scx.object.mapper.primitive_array;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.FloatNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ValueNode;

/// FloatArrayNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class FloatArrayNodeMapper implements NodeMapper<float[]> {

    @Override
    public Node toNode(float[] value, ToNodeContext context) {
        var arrayNode = new ArrayNode(value.length);
        for (var i : value) {
            arrayNode.add(new FloatNode(i));
        }
        return arrayNode;
    }

    @Override
    public float[] fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理数组类型
        if (node instanceof ArrayNode arrayNode) {
            var result = new float[arrayNode.size()];
            var i = 0;
            for (var e : arrayNode) {
                result[i] = toFloat(e);
                i = i + 1;
            }
            return result;
        }
        //3, 非数组类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private float toFloat(Node element) throws NodeMappingException {
        //1, 不允许为 null
        if (element.isNull()) {
            throw new NodeMappingException("Element cannot be null");
        }
        //2, 只处理值类型
        if (element instanceof ValueNode valueNode) {
            try {
                return valueNode.asFloat();
            } catch (NumberFormatException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非值类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + element.getClass());
    }

}
