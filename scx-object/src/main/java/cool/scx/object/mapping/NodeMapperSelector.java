package cool.scx.object.mapping;

import cool.scx.object.mapping.mapper.StringNodeMapper;
import cool.scx.object.mapping.mapper.UntypedNodeMapper;
import cool.scx.object.mapping.mapper.math.BigDecimalNodeMapper;
import cool.scx.object.mapping.mapper.math.BigIntegerNodeMapper;
import cool.scx.object.mapping.mapper.node.*;
import cool.scx.object.mapping.mapper.other.URINodeMapper;
import cool.scx.object.mapping.mapper.other.UUIDNodeMapper;
import cool.scx.object.mapping.mapper.primitive.*;
import cool.scx.object.mapping.mapper.time.DateNodeMapper;
import cool.scx.object.mapping.mapper.time.DurationNodeMapper;
import cool.scx.object.mapping.mapper.time.TemporalAccessorNodeMapper;
import cool.scx.object.mapping.mapper_factory.*;
import cool.scx.object.node.*;
import cool.scx.reflect.TypeInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.reflect.ScxReflect.typeOf;
import static java.time.format.DateTimeFormatter.*;

/// NodeMapperSelector (支持动态扩容)
///
/// @author scx567888
/// @version 0.0.1
public final class NodeMapperSelector {

    //同时缓存 Class 和 TypeInfo, 加速查找
    private final Map<Object, NodeMapper<?>> NODE_MAPPER_MAP = new ConcurrentHashMap<>();

    private final List<NodeMapperFactory> NODE_MAPPER_FACTORY_LIST = new ArrayList<>();

    private final Lock LOCK = new ReentrantLock();

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


        // 时间
        registerNodeMapper(LocalDateTime.class, new TemporalAccessorNodeMapper<>(ISO_LOCAL_DATE_TIME, LocalDateTime::from));
        registerNodeMapper(LocalDate.class, new TemporalAccessorNodeMapper<>(ISO_LOCAL_DATE, LocalDate::from));
        registerNodeMapper(LocalTime.class, new TemporalAccessorNodeMapper<>(ISO_LOCAL_TIME, LocalTime::from));
        registerNodeMapper(OffsetDateTime.class, new TemporalAccessorNodeMapper<>(ISO_OFFSET_DATE_TIME, OffsetDateTime::from));
        registerNodeMapper(OffsetTime.class, new TemporalAccessorNodeMapper<>(ISO_OFFSET_TIME, OffsetTime::from));
        registerNodeMapper(ZonedDateTime.class, new TemporalAccessorNodeMapper<>(ISO_ZONED_DATE_TIME, ZonedDateTime::from));
        registerNodeMapper(Year.class, new TemporalAccessorNodeMapper<>(ofPattern("yyyy"), Year::from));
        registerNodeMapper(Month.class, new TemporalAccessorNodeMapper<>(ofPattern("MM"), Month::from));
        registerNodeMapper(MonthDay.class, new TemporalAccessorNodeMapper<>(ofPattern("--MM-dd"), MonthDay::from));
        registerNodeMapper(YearMonth.class, new TemporalAccessorNodeMapper<>(ofPattern("yyyy-MM"), YearMonth::from));
        registerNodeMapper(DayOfWeek.class, new TemporalAccessorNodeMapper<>(ofPattern("e"), DayOfWeek::from));
        registerNodeMapper(Instant.class, new TemporalAccessorNodeMapper<>(ISO_INSTANT, Instant::from));
        registerNodeMapper(Duration.class, new DurationNodeMapper());
        registerNodeMapper(Date.class, new DateNodeMapper());


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


        // Untyped
        registerNodeMapper(Object.class, new UntypedNodeMapper());


        // Other
        registerNodeMapper(UUID.class, new UUIDNodeMapper());
        registerNodeMapper(URI.class, new URINodeMapper());


        // 注意顺序
        registerNodeMapperFactory(new ArrayNodeMapperFactory());
        registerNodeMapperFactory(new CollectionNodeMapperFactory());
        registerNodeMapperFactory(new MapNodeMapperFactory());
        registerNodeMapperFactory(new BeanNodeMapperFactory());
        registerNodeMapperFactory(new RecordNodeMapperFactory());
        registerNodeMapperFactory(new EnumNodeMapperFactory());


    }

    public <T> void registerNodeMapper(TypeInfo type, NodeMapper<T> typeHandler) {
        NODE_MAPPER_MAP.put(type, typeHandler);
    }

    public <T> void registerNodeMapper(Class<T> type, NodeMapper<T> typeHandler) {
        // 同时注册两份 Class 和 TypeInfo, 方便后续根据 Class 快速查找
        NODE_MAPPER_MAP.put(type, typeHandler);
        NODE_MAPPER_MAP.put(typeOf(type), typeHandler);
    }

    public void registerNodeMapperFactory(NodeMapperFactory nodeMapperFactory) {
        NODE_MAPPER_FACTORY_LIST.add(nodeMapperFactory);
    }

    public void registerNodeMapperFactory(NodeMapperFactory nodeMapperFactory, int order) {
        NODE_MAPPER_FACTORY_LIST.add(order, nodeMapperFactory);
    }

    @SuppressWarnings("unchecked")
    public <T> NodeMapper<T> findNodeMapper(TypeInfo type) throws NodeMappingException {
        var nodeMapper = NODE_MAPPER_MAP.get(type);
        if (nodeMapper != null) {
            return (NodeMapper<T>) nodeMapper;
        }
        LOCK.lock();
        try {
            // 双重检查
            nodeMapper = NODE_MAPPER_MAP.get(type);
            if (nodeMapper != null) {
                return (NodeMapper<T>) nodeMapper;
            }
            nodeMapper = this.createNodeMapper(type);
            NODE_MAPPER_MAP.put(type, nodeMapper);
            return (NodeMapper<T>) nodeMapper;
        } finally {
            LOCK.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> NodeMapper<T> findNodeMapper(Class<T> type) throws NodeMappingException {
        var nodeMapper = NODE_MAPPER_MAP.get(type);
        if (nodeMapper != null) {
            return (NodeMapper<T>) nodeMapper;
        }
        LOCK.lock();
        try {
            // 双重检查
            nodeMapper = NODE_MAPPER_MAP.get(type);
            if (nodeMapper != null) {
                return (NodeMapper<T>) nodeMapper;
            }
            var typeInfo = typeOf(type);
            // 检查是否已经通过 typeInfo 构建
            var oldNodeMapper = NODE_MAPPER_MAP.get(typeInfo);
            if (oldNodeMapper != null) {
                NODE_MAPPER_MAP.put(type, oldNodeMapper);
                return (NodeMapper<T>) oldNodeMapper;
            }
            // 构建新的 nodeMapper
            nodeMapper = this.createNodeMapper(typeInfo);
            // 冗余 key 缓存
            NODE_MAPPER_MAP.put(type, nodeMapper);
            NODE_MAPPER_MAP.put(typeInfo, nodeMapper);
            return (NodeMapper<T>) nodeMapper;
        } finally {
            LOCK.unlock();
        }
    }

    /// 创建新的 NodeMapper
    private NodeMapper<?> createNodeMapper(TypeInfo typeInfo) throws NodeMappingException {
        for (var factory : NODE_MAPPER_FACTORY_LIST) {
            var nodeMapper = factory.createNodeMapper(typeInfo, this);
            if (nodeMapper != null) {
                return nodeMapper;
            }
        }
        throw new NodeMappingException("No NodeMapper found for type " + typeInfo);
    }

}
