package cool.scx.object.mapping.mapper_factory;

import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMapperFactory;
import cool.scx.object.mapping.NodeMapperSelector;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.mapper.CollectionNodeMapper;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.TypeInfo;

import java.util.Collection;

import static cool.scx.reflect.ScxReflect.typeOf;

public class CollectionNodeMapperFactory implements NodeMapperFactory {

    @Override
    public NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) throws NodeMappingException {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Collection.class.isAssignableFrom(typeInfo.rawClass())) {
                var collectionType = classInfo.findSuperType(Collection.class);
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
                var componentNodeMapper = selector.findNodeMapper(componentType);
                return new CollectionNodeMapper(classInfo, componentNodeMapper);
            }
        }
        return null;
    }

}
