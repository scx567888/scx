package cool.scx.object.mapper;

import cool.scx.object.mapper.primitive.*;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.reflect.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeMapperSelector {

    private final Map<Type, NodeMapper<?>> NODE_MAPPER_MAP = new ConcurrentHashMap<>();

    public NodeMapperSelector() {
        // 基本类型
        registerNodeMapper(byte.class, new ByteNodeMapper(true));
        registerNodeMapper(short.class, new ShortNodeMapper(true));
        registerNodeMapper(int.class, new IntegerNodeMapper(true));
        registerNodeMapper(long.class, new LongNodeMapper(true));
        registerNodeMapper(float.class, new FloatNodeMapper(true));
        registerNodeMapper(double.class, new DoubleNodeMapper(true));
        registerNodeMapper(boolean.class, new BooleanNodeMapper(true));
        registerNodeMapper(char.class, new CharacterNodeMapper(true));
        registerNodeMapper(Byte.class, new ByteNodeMapper(false));
        registerNodeMapper(Short.class, new ShortNodeMapper(false));
        registerNodeMapper(Integer.class, new IntegerNodeMapper(false));
        registerNodeMapper(Long.class, new LongNodeMapper(false));
        registerNodeMapper(Float.class, new FloatNodeMapper(false));
        registerNodeMapper(Double.class, new DoubleNodeMapper(false));
        registerNodeMapper(Boolean.class, new BooleanNodeMapper(false));
        registerNodeMapper(Character.class, new CharacterNodeMapper(false));


        //常用类型
        registerNodeMapper(String.class, new StringNodeMapper());
        
    }

    public <T> void registerNodeMapper(Class<T> type, NodeMapper<T> typeHandler) {
        NODE_MAPPER_MAP.put(type, typeHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> NodeMapper<T> findNodeMapper(Type type) {
        return (NodeMapper<T>) NODE_MAPPER_MAP.computeIfAbsent(type, this::createNodeMapper);
    }

    private NodeMapper<?> createNodeMapper(Type type) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Node toNode(Object value) {
        if (value == null) {
            return NullNode.NULL;
        }
        var valueType = ScxReflect.getType(value.getClass());
        // 基本类型我们可以直接处理
        if (valueType instanceof PrimitiveTypeInfo primitiveTypeInfo) {
            var nodeMapper = (NodeMapper<Object>) NODE_MAPPER_MAP.get(primitiveTypeInfo.primitiveClass());
            return nodeMapper.toNode(value, this);
        }
        // 数组类型 todo
        if (valueType instanceof ArrayTypeInfo arrayTypeInfo) {
            return null;
        }
        // 一个类 尝试查找现有 映射器
        if (valueType instanceof ClassInfo classInfo) {
            var nodeMapper = (NodeMapper<Object>) NODE_MAPPER_MAP.get(classInfo.rawClass());
            if (nodeMapper != null) {
                return nodeMapper.toNode(value, this);
            }
            //没找到 判断是不是能够被实例化的类
            //todo 这里先不判断
            var objectNodeMapper = new ObjectNodeMapper(classInfo);

            return objectNodeMapper.toNode(value, this);
        }


        return null;
    }

    public <T> T fromNode(Node node, TypeInfo type) {
        return null;
    }


}
