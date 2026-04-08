package cool.scx.object.mapping.mapper_factory;

import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMapperFactory;
import cool.scx.object.mapping.NodeMapperSelector;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.mapper.map.MapNodeMapper;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.TypeInfo;

import java.util.Map;

import static cool.scx.reflect.ScxReflect.typeOf;

public class MapNodeMapperFactory implements NodeMapperFactory {

    @Override
    public NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) throws NodeMappingException {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (Map.class.isAssignableFrom(typeInfo.rawClass())) {
                var mapType = classInfo.findSuperType(Map.class);
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
                var keyNodeMapper = selector.findNodeMapper(keyType);
                var valueNodeMapper = selector.findNodeMapper(valueType);
                return new MapNodeMapper(classInfo, keyNodeMapper, valueNodeMapper);
            }
        }
        return null;
    }

}
