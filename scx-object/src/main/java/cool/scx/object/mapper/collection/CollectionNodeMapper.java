package cool.scx.object.mapper.collection;

import cool.scx.object.mapper.*;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ClassInfo;

import java.util.*;

import static cool.scx.reflect.ScxReflect.typeOf;

/// CollectionNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class CollectionNodeMapper implements NodeMapper<Collection<?>> {

    private final ClassInfo classInfo;
    private final NodeMapper<Object> componentNodeMapper;

    public CollectionNodeMapper(ClassInfo classInfo, NodeMapperSelector selector) {
        this.classInfo = classInfo;
        var collectionType = this.classInfo.findSuperType(Collection.class);
        if (collectionType == null) {
            throw new IllegalStateException("Collection class not found");
        }
        // 尝试获取 componentType 
        var componentType = collectionType.bindings().get("E");
        // 有可能是 null, 回退到 Object
        if (componentType == null) {
            componentType = typeOf(Object.class);
        }
        // 这个 componentNodeMapper 实际上只能用于 fromNode,
        // 因为在 toNode 的时候,由于 Collection 的泛型是被擦除的,
        // 所以我们是不能假定 每一个元素的类型都是 componentType 
        this.componentNodeMapper = selector.findNodeMapper(componentType);
    }

    @Override
    public Node toNode(Collection<?> value, ToNodeContext context) throws NodeMappingException {
        var arrayNode = new ArrayNode(value.size());
        var i = 0;
        for (var a : value) {
            arrayNode.add(context.toNode(a, i));
            i = i + 1;
        }
        return arrayNode;
    }

    @Override
    public Collection<?> fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ArrayNode 类型
        if (node instanceof ArrayNode arrayNode) {
            Collection<Object> result = createCollection(arrayNode.size());
            for (var n : arrayNode) {
                var i = componentNodeMapper.fromNode(n, context);
                result.add(i);
            }
            return result;
        }
        //3, 非 ArrayNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    private Collection<Object> createCollection(int size) throws NodeMappingException {
        if (classInfo.rawClass() == Collection.class) {
            return new ArrayList<>(size);
        }
        //如果只是 List 那么我们需一个默认的实现 这里使用 ArrayList
        if (classInfo.rawClass() == List.class) {
            return new ArrayList<>(size);
        }
        if (classInfo.rawClass() == Set.class) {
            return new HashSet<>(size);
        }
        if (classInfo.rawClass() == ArrayList.class) {
            return new ArrayList<>(size);
        }
        if (classInfo.rawClass() == LinkedList.class) {
            return new LinkedList<>();
        }
        if (classInfo.rawClass() == HashSet.class) {
            return new HashSet<>(size);
        }
        if (classInfo.rawClass() == TreeSet.class) {
            return new TreeSet<>();
        }
        if (classInfo.rawClass() == LinkedHashSet.class) {
            return new LinkedHashSet<>(size);
        }
        throw new NodeMappingException("Unsupported Collection type: " + classInfo);
    }

}
