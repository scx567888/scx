package cool.scx.jdbc.type_handler;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.jdbc.type_handler.math.BigDecimalTypeHandler;
import cool.scx.jdbc.type_handler.math.BigIntegerTypeHandler;
import cool.scx.jdbc.type_handler.primitive.*;
import cool.scx.jdbc.type_handler.time.*;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cool.scx.reflect.ClassUtils.getEnumClass;
import static cool.scx.reflect.ClassUtils.isEnum;

/// TypeHandlerSelector
///
/// @author scx567888
/// @version 0.0.1
public final class TypeHandlerSelector {

    private final Map<Type, TypeHandler<?>> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();

    public TypeHandlerSelector() {
        // 基本类型
        registerTypeHandler(boolean.class, new BooleanTypeHandler(true));
        registerTypeHandler(char.class, new CharacterTypeHandler(true));
        registerTypeHandler(byte.class, new ByteTypeHandler(true));
        registerTypeHandler(short.class, new ShortTypeHandler(true));
        registerTypeHandler(int.class, new IntegerTypeHandler(true));
        registerTypeHandler(long.class, new LongTypeHandler(true));
        registerTypeHandler(float.class, new FloatTypeHandler(true));
        registerTypeHandler(double.class, new DoubleTypeHandler(true));
        registerTypeHandler(Boolean.class, new BooleanTypeHandler(false));
        registerTypeHandler(Character.class, new CharacterTypeHandler(false));
        registerTypeHandler(Byte.class, new ByteTypeHandler(false));
        registerTypeHandler(Short.class, new ShortTypeHandler(false));
        registerTypeHandler(Integer.class, new IntegerTypeHandler(false));
        registerTypeHandler(Long.class, new LongTypeHandler(false));
        registerTypeHandler(Float.class, new FloatTypeHandler(false));
        registerTypeHandler(Double.class, new DoubleTypeHandler(false));


        // 常用类型
        registerTypeHandler(String.class, new StringTypeHandler());
        registerTypeHandler(Byte[].class, new ByteObjectArrayTypeHandler());
        registerTypeHandler(byte[].class, new ByteArrayTypeHandler());


        // 大数字
        registerTypeHandler(BigInteger.class, new BigIntegerTypeHandler());
        registerTypeHandler(BigDecimal.class, new BigDecimalTypeHandler());


        // 时间
        registerTypeHandler(LocalDateTime.class, new LocalDateTimeTypeHandler());
        registerTypeHandler(LocalDate.class, new LocalDateTypeHandler());
        registerTypeHandler(LocalTime.class, new LocalTimeTypeHandler());
        registerTypeHandler(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        registerTypeHandler(OffsetTime.class, new OffsetTimeTypeHandler());
        registerTypeHandler(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        registerTypeHandler(Month.class, new MonthTypeHandler());
        registerTypeHandler(Year.class, new YearTypeHandler());
        registerTypeHandler(YearMonth.class, new YearMonthTypeHandler());
        registerTypeHandler(Date.class, new DateTypeHandler());
        registerTypeHandler(Instant.class, new InstantTypeHandler());
        registerTypeHandler(Duration.class, new DurationTypeHandler());


        //clob and blow
        registerTypeHandler(InputStream.class, new BlobInputStreamTypeHandler());
        registerTypeHandler(Reader.class, new ClobReaderTypeHandler());
    }

    public <T> void registerTypeHandler(Class<T> type, TypeHandler<T> typeHandler) {
        TYPE_HANDLER_MAP.put(type, typeHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeHandler<T> findTypeHandler(Type type) {
        return (TypeHandler<T>) TYPE_HANDLER_MAP.computeIfAbsent(type, this::createTypeHandler);
    }

    private TypeHandler<?> createTypeHandler(Type type) {
        if (type instanceof Class<?> clazz) {
            //普通 class 直接创建 失败后回退到 ObjectTypeHandler
            var c = createTypeHandler0(clazz);
            if (c != null) {
                return c;
            }
        } else if (type instanceof JavaType javaType) {
            //JavaType 先尝试使用 getRawClass 进行创建 失败后回退到 ObjectTypeHandler
            var c = createTypeHandler0(javaType.getRawClass());
            if (c != null) {
                return c;
            }
        }
        return new ObjectTypeHandler(type);
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> TypeHandler<?> createTypeHandler0(Class<?> clazz) {
        if (isEnum(clazz)) {
            var enumClass = getEnumClass(clazz);
            return new EnumTypeHandler<>((Class<E>) enumClass);
        } else {
            //判断是否为可识别类型的子类
            for (var entry : TYPE_HANDLER_MAP.entrySet()) {
                if (entry.getKey() instanceof Class<?> c && c.isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

}
