package cool.scx.object.mapper.collection;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.TypeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// todo 待优化
public class CollectionNodeMapper implements NodeMapper<Collection<?>> {

    private final ClassInfo classInfo;

    public CollectionNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public Node toNode(Collection<?> value, NodeMapperSelector selector) {
        var arrayNode = new ArrayNode();
        for (var o : value) {
            arrayNode.add(selector.toNode(o));
        }
        return arrayNode;
    }

    @Override
    public Collection<?> fromNode(Node node, NodeMapperSelector selector) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ArrayNode arrayNode) {
            Collection<Object> result;
            TypeInfo componentType;
            //如果只是 List 那么我们需一个默认的实现 这里使用 ArrayList
            if (classInfo.rawClass() == List.class) {
                result = new ArrayList<>();
                componentType = classInfo.bindings().get("E");
            } else {
                throw new IllegalArgumentException("");
            }
            for (var n : arrayNode) {
                var o = selector.fromNode(n, componentType);
                result.add(o);
            }
            return result;
        }
        throw new IllegalArgumentException("不是 arrayNode");
    }

}
