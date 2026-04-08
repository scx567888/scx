package cool.scx.object.mapping.mapper;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ClassInfo;

import java.util.*;

/// CollectionNodeMapper
///
/// 支持 单值包裹为 单值集合
///
/// @author scx567888
/// @version 0.0.1
public final class CollectionNodeMapper implements NodeMapper<Collection<?>> {

    private final ClassInfo classInfo;
    private final NodeMapper<Object> componentNodeMapper;

    public CollectionNodeMapper(ClassInfo classInfo, NodeMapper<Object> componentNodeMapper) {
        this.classInfo = classInfo;
        this.componentNodeMapper = componentNodeMapper;
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
        //2, 先处理 ArrayNode 类型
        if (node instanceof ArrayNode arrayNode) {
            Collection<Object> result = createCollection(arrayNode.size());
            for (var n : arrayNode) {
                var i = componentNodeMapper.fromNode(n, context);
                result.add(i);
            }
            return result;
        }
        //3, 尝试宽容处理 单值包裹为 单值集合
        Collection<Object> result = createCollection(1);
        var i = componentNodeMapper.fromNode(node, context);
        result.add(i);
        return result;
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
