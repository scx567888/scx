package cool.scx.object.mapper.map;

import cool.scx.object.mapper.*;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.object.node.TextNode;
import cool.scx.object.node.ValueNode;
import cool.scx.reflect.ClassInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static cool.scx.reflect.ScxReflect.typeOf;

/// MapNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class MapNodeMapper implements NodeMapper<Map<?, ?>> {

    private final ClassInfo classInfo;
    private final NodeMapper<Object> keyNodeMapper;
    private final NodeMapper<Object> valueNodeMapper;

    public MapNodeMapper(ClassInfo classInfo, NodeMapperSelector selector) {
        this.classInfo = classInfo;
        var mapType = this.classInfo.findSuperType(Map.class);
        if (mapType == null) {
            throw new IllegalStateException("Map class not found");
        }
        var keyType = mapType.bindings().get("K");
        // 为 null 回退到 String
        if (keyType == null) {
            keyType = typeOf(String.class);
        }
        // 为 null 回退到 Object
        var valueType = mapType.bindings().get("V");
        if (valueType == null) {
            valueType = typeOf(Object.class);
        }
        // 这个 keyNodeMapper 和 valueNodeMapper 实际上只能用于 fromNode,
        // 因为在 toNode 的时候,由于 Map 的泛型是被擦除的,
        // 所以我们是不能假定 每一个元素的类型都是 keyType, valueType
        this.keyNodeMapper = selector.findNodeMapper(keyType);
        this.valueNodeMapper = selector.findNodeMapper(valueType);
    }

    @Override
    public Node toNode(Map<?, ?> mapValue, ToNodeContext context) throws NodeMappingException {
        var objectNode = new ObjectNode(mapValue.size());
        for (var e : mapValue.entrySet()) {
            var key = e.getKey();
            var value = e.getValue();
            //处理忽略 null value
            if (value == null && context.options().ignoreNullValue()) {
                continue;
            }
            var k = toKey(key, context);
            var v = context.toNode(value, k);
            objectNode.put(k, v);
        }
        return objectNode;
    }

    @Override
    public Map<?, ?> fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ObjectNode 类型
        if (node instanceof ObjectNode objectNode) {
            Map<Object, Object> result = createMap(objectNode.size());
            for (var n : objectNode) {
                //此处为了方便其他的 NodeMapper 处理, 将 String 包装为 TextNode
                var k = keyNodeMapper.fromNode(new TextNode(n.getKey()), context);
                var v = valueNodeMapper.fromNode(n.getValue(), context);
                result.put(k, v);
            }
            return result;
        }
        //3, 非 ObjectNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private String toKey(Object value, ToNodeContext context) throws NodeMappingException {
        //1, 尝试将 key 转换为 String
        var node = context.toNode(value, null);//todo 这里的 pathSegment 应该放什么? 
        //2, 处理 nullKey
        if (node.isNull()) {
            return context.options().nullKey();
        }
        //3, 处理 ValueNode
        if (node instanceof ValueNode valueNode) {
            return valueNode.asText();
        }
        //4, 其余类型直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private Map<Object, Object> createMap(int size) throws NodeMappingException {
        //如果只是 Map 那么我们需一个默认的实现 这里使用 LinkedHashMap
        if (classInfo.rawClass() == Map.class) {
            return new LinkedHashMap<>(size);
        }
        if (classInfo.rawClass() == HashMap.class) {
            return new HashMap<>(size);
        }
        if (classInfo.rawClass() == LinkedHashMap.class) {
            return new LinkedHashMap<>(size);
        }
        if (classInfo.rawClass() == TreeMap.class) {
            return new TreeMap<>();
        }
        if (classInfo.rawClass() == ConcurrentHashMap.class) {
            return new ConcurrentHashMap<>(size);
        }
        throw new NodeMappingException("Unsupported Map type: " + classInfo);
    }

}
