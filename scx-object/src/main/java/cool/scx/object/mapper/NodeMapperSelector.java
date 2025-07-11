package cool.scx.object.mapper;

import cool.scx.object.mapper.collection.CollectionNodeMapper;
import cool.scx.object.mapper.map.MapNodeMapper;
import cool.scx.object.mapper.math.BigDecimalNodeMapper;
import cool.scx.object.mapper.math.BigIntegerNodeMapper;
import cool.scx.object.mapper.primitive.*;
import cool.scx.object.mapper.primitive_array.*;
import cool.scx.object.mapper.time.LocalDateTimeNodeMapper;
import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.reflect.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeMapperSelector {

    private final Map<Type, NodeMapper<?>> NODE_MAPPER_MAP = new ConcurrentHashMap<>();

    public NodeMapperSelector() {

        // 基本类型
        registerNodeMapper(byte.class, new ByteNodeMapper(true));
        registerNodeMapper(short.class, new ShortNodeMapper(true));
        registerNodeMapper(int.class, new IntNodeMapper(true));
        registerNodeMapper(long.class, new LongNodeMapper(true));
        registerNodeMapper(float.class, new FloatNodeMapper(true));
        registerNodeMapper(double.class, new DoubleNodeMapper(true));
        registerNodeMapper(boolean.class, new BooleanNodeMapper(true));
        registerNodeMapper(char.class, new CharNodeMapper(true));
        registerNodeMapper(Byte.class, new ByteNodeMapper(false));
        registerNodeMapper(Short.class, new ShortNodeMapper(false));
        registerNodeMapper(Integer.class, new IntNodeMapper(false));
        registerNodeMapper(Long.class, new LongNodeMapper(false));
        registerNodeMapper(Float.class, new FloatNodeMapper(false));
        registerNodeMapper(Double.class, new DoubleNodeMapper(false));
        registerNodeMapper(Boolean.class, new BooleanNodeMapper(false));
        registerNodeMapper(Character.class, new CharNodeMapper(false));


        //常用类型
        registerNodeMapper(String.class, new StringNodeMapper());


        //大数字类型
        registerNodeMapper(BigInteger.class, new BigIntegerNodeMapper());
        registerNodeMapper(BigDecimal.class, new BigDecimalNodeMapper());

        
        //基本类型数组类型
        registerNodeMapper(byte[].class, new ByteArrayNodeMapper());
        registerNodeMapper(short[].class, new ShortArrayNodeMapper());
        registerNodeMapper(int[].class, new IntArrayNodeMapper());
        registerNodeMapper(long[].class, new LongArrayNodeMapper());
        registerNodeMapper(float[].class, new FloatArrayNodeMapper());
        registerNodeMapper(double[].class, new DoubleArrayNodeMapper());
        registerNodeMapper(boolean[].class, new BooleanArrayNodeMapper());
        registerNodeMapper(char[].class, new CharArrayNodeMapper());


        // 时间
        registerNodeMapper(LocalDateTime.class, new LocalDateTimeNodeMapper());

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
        //进行判断
        var valueType = ScxReflect.getType(value.getClass());
        //优先使用 MAP 查找处理
        var nodeMapper = (NodeMapper<Object>) NODE_MAPPER_MAP.get(valueType.rawClass());
        if (nodeMapper != null) {
            return nodeMapper.toNode(value, this);
        }
        // 基本类型
        if (valueType instanceof PrimitiveTypeInfo primitiveTypeInfo) {
            //理论上不可能走到这里
            throw new IllegalStateException("");
        }
        // 数组类型
        if (valueType instanceof ArrayTypeInfo arrayTypeInfo) {
            //这里理论上不可能有 基本类型数组了
            return null;
        }
        // 一个类
        if (valueType instanceof ClassInfo classInfo) {
            //特化处理 Map 和 Collection
            if (value instanceof Map<?, ?> map) {
                var mapNodeMapper = new MapNodeMapper(classInfo);
                return mapNodeMapper.toNode(map, this);
            }
            if (value instanceof Collection<?> collection) {
                var collectionNodeMapper = new CollectionNodeMapper(classInfo);
                return collectionNodeMapper.toNode(collection, this);
            }
            //没找到 判断是不是能够被实例化的类
            //todo 这里先不判断
            var objectNodeMapper = new ObjectNodeMapper(classInfo);

            return objectNodeMapper.toNode(value, this);
        }


        return null;
    }

    public <T> T fromNode(Node node, Class<T> type) {
        return fromNode(node, ScxReflect.getType(type));
    }

    @SuppressWarnings("unchecked")
    public <T> T fromNode(Node node, TypeInfo type) {
        var nodeMapper = NODE_MAPPER_MAP.get(type.rawClass());
        if (nodeMapper != null) {
            return (T) nodeMapper.fromNode(node, this);
        }

        if (type instanceof PrimitiveTypeInfo primitiveTypeInfo) {
            throw new IllegalStateException("");
        }
        if (type instanceof ArrayTypeInfo arrayTypeInfo) {
            throw new IllegalStateException("");
        }
        if (type instanceof ClassInfo classInfo) {
            if (Map.class.isAssignableFrom(type.rawClass())) {
                var mapNodeMapper = new MapNodeMapper(classInfo);
                return (T) mapNodeMapper.fromNode(node, this);
            }
            if (Collection.class.isAssignableFrom(type.rawClass())) {
                var collectionNodeMapper = new CollectionNodeMapper(classInfo);
                return (T) collectionNodeMapper.fromNode(node, this);
            }
            if (classInfo.rawClass() == Object.class) {
                var untypedNodeMapper = new UntypedNodeMapper();
                return (T) untypedNodeMapper.fromNode(node, this);
            }

            //todo 这里先不判断
            var objectNodeMapper = new ObjectNodeMapper(classInfo);

            return (T) objectNodeMapper.fromNode(node, this);
        }
        return null;
    }


}
