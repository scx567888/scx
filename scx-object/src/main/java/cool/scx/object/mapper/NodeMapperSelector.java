package cool.scx.object.mapper;

import cool.scx.object.mapper.collection.CollectionNodeMapper;
import cool.scx.object.mapper.enumeration.EnumNodeMapper;
import cool.scx.object.mapper.map.MapNodeMapper;
import cool.scx.object.mapper.math.BigDecimalNodeMapper;
import cool.scx.object.mapper.math.BigIntegerNodeMapper;
import cool.scx.object.mapper.node.*;
import cool.scx.object.mapper.object.ObjectArrayNodeMapper;
import cool.scx.object.mapper.object.ObjectNodeMapper;
import cool.scx.object.mapper.primitive.*;
import cool.scx.object.mapper.primitive_array.*;
import cool.scx.object.mapper.record.RecordNodeMapper;
import cool.scx.object.mapper.string.StringNodeMapper;
import cool.scx.object.mapper.time.LocalDateNodeMapper;
import cool.scx.object.mapper.time.LocalDateTimeNodeMapper;
import cool.scx.object.mapper.time.LocalTimeNodeMapper;
import cool.scx.object.mapper.untyped.UntypedNodeMapper;
import cool.scx.object.node.*;
import cool.scx.reflect.ArrayTypeInfo;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.PrimitiveTypeInfo;
import cool.scx.reflect.TypeInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.constant.DateTimeFormatters.*;
import static cool.scx.reflect.ScxReflect.typeOf;

/// NodeMapperSelector (支持动态扩容)
///
/// @author scx567888
/// @version 0.0.1
public final class NodeMapperSelector {

    //同时缓存 Class 和 TypeInfo, 加速查找
    private final Map<Object, NodeMapper<?>> NODE_MAPPER_MAP = new HashMap<>();

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


        // 基本类型包装类型
        registerNodeMapper(Byte.class, new ByteNodeMapper(false));
        registerNodeMapper(Short.class, new ShortNodeMapper(false));
        registerNodeMapper(Integer.class, new IntNodeMapper(false));
        registerNodeMapper(Long.class, new LongNodeMapper(false));
        registerNodeMapper(Float.class, new FloatNodeMapper(false));
        registerNodeMapper(Double.class, new DoubleNodeMapper(false));
        registerNodeMapper(Boolean.class, new BooleanNodeMapper(false));
        registerNodeMapper(Character.class, new CharNodeMapper(false));


        // 字符串
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
        registerNodeMapper(LocalDateTime.class, new LocalDateTimeNodeMapper(yyyy_MM_dd_HH_mm_ss));
        registerNodeMapper(LocalDate.class, new LocalDateNodeMapper(yyyy_MM_dd));
        registerNodeMapper(LocalTime.class, new LocalTimeNodeMapper(HH_mm_ss));


        // Node 类型
        registerNodeMapper(IntNode.class, new IntNodeNodeMapper());
        registerNodeMapper(LongNode.class, new LongNodeNodeMapper());
        registerNodeMapper(FloatNode.class, new FloatNodeNodeMapper());
        registerNodeMapper(DoubleNode.class, new DoubleNodeNodeMapper());
        registerNodeMapper(BigIntegerNode.class, new BigIntegerNodeNodeMapper());
        registerNodeMapper(BigDecimalNode.class, new BigDecimalNodeNodeMapper());
        registerNodeMapper(TextNode.class, new TextNodeNodeMapper());
        registerNodeMapper(BooleanNode.class, new BooleanNodeNodeMapper());
        registerNodeMapper(ValueNode.class, new ValueNodeNodeMapper());
        registerNodeMapper(NumberNode.class, new NumericNodeNodeMapper());
        registerNodeMapper(NullNode.class, new NullNodeNodeMapper());
        registerNodeMapper(ArrayNode.class, new ArrayNodeNodeMapper());
        registerNodeMapper(ObjectNode.class, new ObjectNodeNodeMapper());
        registerNodeMapper(Node.class, new NodeNodeMapper());


        // 特殊类型
        registerNodeMapper(Object.class, new UntypedNodeMapper());

    }

    public <T> void registerNodeMapper(TypeInfo type, NodeMapper<T> typeHandler) {
        NODE_MAPPER_MAP.put(type, typeHandler);
    }

    public <T> void registerNodeMapper(Class<T> type, NodeMapper<T> typeHandler) {
        // 同时注册两份 Class 和 TypeInfo, 方便后续根据 Class 快速查找
        NODE_MAPPER_MAP.put(type, typeHandler);
        NODE_MAPPER_MAP.put(typeOf(type), typeHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> NodeMapper<T> findNodeMapper(TypeInfo type) {
        var nodeMapper = (NodeMapper<T>) NODE_MAPPER_MAP.get(type);
        if (nodeMapper != null) {
            return nodeMapper;
        }
        var newNodeMapper = (NodeMapper<T>) this.createNodeMapper(type);
        registerNodeMapper(type, newNodeMapper);
        return newNodeMapper;
    }

    @SuppressWarnings("unchecked")
    public <T> NodeMapper<T> findNodeMapper(Class<T> type) {
        var nodeMapper = (NodeMapper<T>) NODE_MAPPER_MAP.get(type);
        if (nodeMapper != null) {
            return nodeMapper;
        }
        var newNodeMapper = (NodeMapper<T>) this.createNodeMapper(type);
        registerNodeMapper(type, newNodeMapper);
        return newNodeMapper;
    }

    private NodeMapper<?> createNodeMapper(Object type) throws NodeMappingException {
        var typeInfo = switch (type) {
            case TypeInfo t -> t;
            case Class<?> c -> typeOf(c);
            // 理论上这是不可达的
            default -> throw new NodeMappingException("Type 类型异常");
        };
        switch (typeInfo) {
            // 理论上不可能走到这里
            case PrimitiveTypeInfo _ -> throw new NodeMappingException("不可达异常 !!!");
            // 这里理论上不可能有 基本类型数组了
            case ArrayTypeInfo arrayTypeInfo -> {
                var componentType = arrayTypeInfo.componentType();
                var componentNodeMapper = findNodeMapper(componentType);
                return new ObjectArrayNodeMapper(arrayTypeInfo, componentNodeMapper);
            }
            case ClassInfo classInfo -> {
                // 特化处理 Map 和 Collection
                if (Map.class.isAssignableFrom(typeInfo.rawClass())) {
                    return new MapNodeMapper(classInfo, this);
                }
                if (Collection.class.isAssignableFrom(classInfo.rawClass())) {
                    return new CollectionNodeMapper(classInfo, this);
                }
                // 这里 我们之所以不处理接口和 抽象类原因如下
                // 在 toNode 的时候 classInfo 是从实例所获取 所以永远不可能是 抽象类 或 接口
                // 在 fromNode 的时候 抽象类 和 接口 我们根本没办法实例化 所以这里直接抛异常 
                return switch (classInfo.classKind()) {
                    case RECORD -> new RecordNodeMapper(classInfo);
                    case CLASS -> {
                        if (classInfo.isAbstract()) {
                            throw new NodeMappingException("无法处理抽象类: " + classInfo);
                        }
                        yield new ObjectNodeMapper(classInfo);
                    }
                    case ENUM -> new EnumNodeMapper<>(classInfo);
                    case ANNOTATION -> throw new NodeMappingException("无法处理注解: " + classInfo);
                    case INTERFACE -> throw new NodeMappingException("无法处理接口: " + classInfo);
                };
            }
        }
    }

}
