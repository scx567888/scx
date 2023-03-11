package cool.scx.sql;

import cool.scx.sql.type_handler.*;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TypeHandlerRegistry {

    private static final Map<Type, TypeHandler<?>> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();

    static {
        //Calendar
        //Blob
        //Duration
        TYPE_HANDLER_MAP.put(Boolean.class, new BooleanTypeHandler());
        TYPE_HANDLER_MAP.put(boolean.class, new BooleanTypeHandler());
        TYPE_HANDLER_MAP.put(Double.class, new DoubleTypeHandler());
        TYPE_HANDLER_MAP.put(double.class, new DoubleTypeHandler());
        TYPE_HANDLER_MAP.put(Byte.class, new ByteTypeHandler());
        TYPE_HANDLER_MAP.put(byte.class, new ByteTypeHandler());
        TYPE_HANDLER_MAP.put(Short.class, new ShortTypeHandler());
        TYPE_HANDLER_MAP.put(short.class, new ShortTypeHandler());
        TYPE_HANDLER_MAP.put(Integer.class, new IntegerTypeHandler());
        TYPE_HANDLER_MAP.put(int.class, new IntegerTypeHandler());
        TYPE_HANDLER_MAP.put(Long.class, new LongTypeHandler());
        TYPE_HANDLER_MAP.put(long.class, new LongTypeHandler());
        TYPE_HANDLER_MAP.put(Float.class, new FloatTypeHandler());
        TYPE_HANDLER_MAP.put(float.class, new FloatTypeHandler());
        TYPE_HANDLER_MAP.put(Reader.class, new ClobReaderTypeHandler());
        TYPE_HANDLER_MAP.put(String.class, new StringTypeHandler());
        TYPE_HANDLER_MAP.put(BigInteger.class, new BigIntegerTypeHandler());
        TYPE_HANDLER_MAP.put(BigDecimal.class, new BigDecimalTypeHandler());
        TYPE_HANDLER_MAP.put(InputStream.class, new BlobInputStreamTypeHandler());
        TYPE_HANDLER_MAP.put(Byte[].class, new ByteObjectArrayTypeHandler());
        TYPE_HANDLER_MAP.put(byte[].class, new ByteArrayTypeHandler());
        TYPE_HANDLER_MAP.put(Date.class, new DateTypeHandler());
        TYPE_HANDLER_MAP.put(Instant.class, new InstantTypeHandler());
        TYPE_HANDLER_MAP.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(LocalDate.class, new LocalDateTypeHandler());
        TYPE_HANDLER_MAP.put(LocalTime.class, new LocalTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetTime.class, new OffsetTimeTypeHandler());
        TYPE_HANDLER_MAP.put(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(Month.class, new MonthTypeHandler());
        TYPE_HANDLER_MAP.put(Year.class, new YearTypeHandler());
        TYPE_HANDLER_MAP.put(YearMonth.class, new YearMonthTypeHandler());
        TYPE_HANDLER_MAP.put(Character.class, new CharacterTypeHandler());
        TYPE_HANDLER_MAP.put(char.class, new CharacterTypeHandler());
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeHandler<T> getTypeHandler(Type type) {
        var handler = TYPE_HANDLER_MAP.computeIfAbsent(type, TypeHandlerRegistry::createTypeHandler);
        if (handler == null) {
            throw new IllegalArgumentException("未找到合适的 TypeHandler !!! : " + type);
        }
        return (TypeHandler<T>) handler;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> TypeHandler<?> createTypeHandler(Type type) {
        if (type instanceof Class<?> clazz) {
            if (Enum.class.isAssignableFrom(clazz)) {
                var enumClass = clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz;
                return new EnumTypeHandler<>((Class<E>) enumClass);
            } else {
                return new ObjectTypeHandler(clazz);
            }
        }
        return null;
    }

}
