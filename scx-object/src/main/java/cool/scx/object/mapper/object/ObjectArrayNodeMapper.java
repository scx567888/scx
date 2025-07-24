package cool.scx.object.mapper.object;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ArrayTypeInfo;

/// ObjectArrayNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectArrayNodeMapper implements NodeMapper<Object[]> {

    private final ArrayTypeInfo arrayTypeInfo;
    private final NodeMapper<Object> componentNodeMapper;

    public ObjectArrayNodeMapper(ArrayTypeInfo arrayTypeInfo, NodeMapper<Object> componentNodeMapper) {
        this.arrayTypeInfo = arrayTypeInfo;
        // 这个只能用于 fromNode, 因为 toNode 有可能是 Object[]
        this.componentNodeMapper = componentNodeMapper;
    }

    @Override
    public Node toNode(Object[] value, ToNodeContext context) throws NodeMappingException {
        var arrayNode = new ArrayNode(value.length);
        var i = 0;
        for (var a : value) {
            arrayNode.add(context.toNode(a, i));
            i = i + 1;
        }
        return arrayNode;
    }

    @Override
    public Object[] fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ArrayNode 类型
        if (node instanceof ArrayNode arrayNode) {
            var result = (Object[]) arrayTypeInfo.newArray(arrayNode.size());
            var i = 0;
            for (var e : arrayNode) {
                result[i] = componentNodeMapper.fromNode(e, context);
                i = i + 1;
            }
            return result;
        }
        //3, 非 ArrayNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
