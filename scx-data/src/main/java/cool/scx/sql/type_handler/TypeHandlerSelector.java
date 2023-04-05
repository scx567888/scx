package cool.scx.sql.type_handler;

import cool.scx.sql.type_handler.math.BigDecimalTypeHandler;
import cool.scx.sql.type_handler.math.BigIntegerTypeHandler;
import cool.scx.sql.type_handler.primitive.*;
import cool.scx.sql.type_handler.time.*;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cool.scx.util.reflect.ClassUtils.isEnum;

public final class TypeHandlerSelector {

    private static final Map<Type, TypeHandler<?>> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();

    static {
        // 基本类型
        TYPE_HANDLER_MAP.put(boolean.class, new BooleanTypeHandler(true));
        TYPE_HANDLER_MAP.put(char.class, new CharacterTypeHandler(true));
        TYPE_HANDLER_MAP.put(byte.class, new ByteTypeHandler(true));
        TYPE_HANDLER_MAP.put(short.class, new ShortTypeHandler(true));
        TYPE_HANDLER_MAP.put(int.class, new IntegerTypeHandler(true));
        TYPE_HANDLER_MAP.put(long.class, new LongTypeHandler(true));
        TYPE_HANDLER_MAP.put(float.class, new FloatTypeHandler(true));
        TYPE_HANDLER_MAP.put(double.class, new DoubleTypeHandler(true));
        TYPE_HANDLER_MAP.put(Boolean.class, new BooleanTypeHandler(false));
        TYPE_HANDLER_MAP.put(Character.class, new CharacterTypeHandler(false));
        TYPE_HANDLER_MAP.put(Byte.class, new ByteTypeHandler(false));
        TYPE_HANDLER_MAP.put(Short.class, new ShortTypeHandler(false));
        TYPE_HANDLER_MAP.put(Integer.class, new IntegerTypeHandler(false));
        TYPE_HANDLER_MAP.put(Long.class, new LongTypeHandler(false));
        TYPE_HANDLER_MAP.put(Float.class, new FloatTypeHandler(false));
        TYPE_HANDLER_MAP.put(Double.class, new DoubleTypeHandler(false));


        // 常用类型
        TYPE_HANDLER_MAP.put(String.class, new StringTypeHandler());
        TYPE_HANDLER_MAP.put(Byte[].class, new ByteObjectArrayTypeHandler());
        TYPE_HANDLER_MAP.put(byte[].class, new ByteArrayTypeHandler());


        // 大数字
        TYPE_HANDLER_MAP.put(BigInteger.class, new BigIntegerTypeHandler());
        TYPE_HANDLER_MAP.put(BigDecimal.class, new BigDecimalTypeHandler());


        // 时间
        TYPE_HANDLER_MAP.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(LocalDate.class, new LocalDateTypeHandler());
        TYPE_HANDLER_MAP.put(LocalTime.class, new LocalTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetTime.class, new OffsetTimeTypeHandler());
        TYPE_HANDLER_MAP.put(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(Month.class, new MonthTypeHandler());
        TYPE_HANDLER_MAP.put(Year.class, new YearTypeHandler());
        TYPE_HANDLER_MAP.put(YearMonth.class, new YearMonthTypeHandler());
        TYPE_HANDLER_MAP.put(Date.class, new DateTypeHandler());
        TYPE_HANDLER_MAP.put(Instant.class, new InstantTypeHandler());
        TYPE_HANDLER_MAP.put(Duration.class, new DurationTypeHandler());

        //clob and blow
        TYPE_HANDLER_MAP.put(InputStream.class, new BlobInputStreamTypeHandler());
        TYPE_HANDLER_MAP.put(Reader.class, new ClobReaderTypeHandler());
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeHandler<T> findTypeHandler(Type type) {
        return (TypeHandler<T>) TYPE_HANDLER_MAP.computeIfAbsent(type, TypeHandlerSelector::createTypeHandler);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> TypeHandler<?> createTypeHandler(Type type) {
        if (type instanceof Class<?> clazz) {
            if (isEnum(clazz)) {
                var enumClass = clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz;
                return new EnumTypeHandler<>((Class<E>) enumClass);
            } else {
                //判断是否为可识别类型的子类
                for (var entry : TYPE_HANDLER_MAP.entrySet()) {
                    if (entry.getKey() instanceof Class<?> c && c.isAssignableFrom(clazz)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return new ObjectTypeHandler(type);
    }

}
