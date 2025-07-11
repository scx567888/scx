package cool.scx.object.mapper.map;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.*;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.TypeInfo;

import java.util.LinkedHashMap;
import java.util.Map;

//todo 待优化
public class MapNodeMapper implements NodeMapper<Map<?, ?>> {

    private final ClassInfo classInfo;

    //todo 和 CollectionNodeMapper 还有 ObjectNodeMapper 一样 是否需要 通过构造函数传递 还是 通过 fromNode 参数传递 ? 
    public MapNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public Node toNode(Map<?, ?> value, NodeMapperSelector selector) {
        var objectNode = new ObjectNode();
        for (var entry : value.entrySet()) {
            var k = entry.getKey();
            var v = entry.getValue();
            var nodeKey = selector.toNode(k);//todo null ? 或者自定义序列化?
            String key;
            if (nodeKey instanceof ValueNode valueNode) {
                key = valueNode.asText();
            } else {
                throw new IllegalArgumentException("key must be of type ValueNode");
            }
            var nodeValue = selector.toNode(v);
            objectNode.put(key, nodeValue);
        }
        return objectNode;
    }

    @Override
    public Map<?, ?> fromNode(Node node, NodeMapperSelector selector) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ObjectNode objectNode) {
            Map<Object, Object> result;
            TypeInfo keyType;
            TypeInfo valueType;
            //如果只是 Map 那么我们需一个默认的实现 这里使用 LinkedHashMap
            if (classInfo.rawClass() == Map.class) {
                result = new LinkedHashMap<>();
                keyType = classInfo.bindings().get("K");
                valueType = classInfo.bindings().get("V");
            } else {
                throw new IllegalArgumentException("");
            }
            for (var n : objectNode) {
                var key = n.getKey();
                var value = n.getValue();
                var k = selector.fromNode(new TextNode(key), keyType);
                var v = selector.fromNode(value, valueType);
                result.put(k, v);
            }
            return result;
        }
        throw new IllegalArgumentException("不是 objectNode");
    }

}
