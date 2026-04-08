package cool.scx.object.mapping.mapper.map;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.*;
import cool.scx.reflect.ClassInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/// MapNodeMapper
///
/// 支持 单值数组解包
///
/// @author scx567888
/// @version 0.0.1
public final class MapNodeMapper implements NodeMapper<Map<?, ?>> {

    private static final MapNodeMapperOptions MAP_NODE_MAPPER_OPTIONS = new MapNodeMapperOptions();

    private final ClassInfo classInfo;
    private final NodeMapper<Object> keyNodeMapper;
    private final NodeMapper<Object> valueNodeMapper;

    public MapNodeMapper(ClassInfo classInfo, NodeMapper<Object> keyNodeMapper, NodeMapper<Object> valueNodeMapper) {
        this.classInfo = classInfo;
        this.keyNodeMapper = keyNodeMapper;
        this.valueNodeMapper = valueNodeMapper;
    }

    @Override
    public Node toNode(Map<?, ?> mapValue, ToNodeContext context) throws NodeMappingException {
        var options = context.options().getMapperOptions(MapNodeMapperOptions.class, MAP_NODE_MAPPER_OPTIONS);
        var objectNode = new ObjectNode(mapValue.size());
        for (var e : mapValue.entrySet()) {
            var key = e.getKey();
            var value = e.getValue();
            //处理忽略 null value
            if (value == null && options.ignoreNullValue()) {
                continue;
            }
            var k = toKey(key, context, options);
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
        //3, 尝试处理 单值数组 (这里假设 ArrayNode 不存在自引用)
        if (node instanceof ArrayNode arrayNode && arrayNode.size() == 1) {
            return this.fromNode(arrayNode.get(0), context);
        }
        //4, 非 ObjectNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private String toKey(Object value, ToNodeContext context, MapNodeMapperOptions options) throws NodeMappingException {
        //1, 尝试将 key 转换为 String
        var node = context.toNode(value, null);//todo 这里的 pathSegment 应该放什么? 
        //2, 处理 nullKey
        if (node.isNull()) {
            return options.nullKey();
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
