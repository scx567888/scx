package cool.scx.jdbc.standard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

public enum StandardDataType {

    //***************** 整数 ********************

    /**
     * 微小的整数类型 取值范围参照 {@link Byte}
     */
    TINYINT,

    /**
     * 较小的整数类型 取值范围参照 {@link Short}
     */
    SMALLINT,

    /**
     * 整数类型 取值范围参照 {@link Integer}
     */
    INT,

    /**
     * 非常大的整数类型 取值范围参照 {@link Long}
     */
    BIGINT,

    //**************** 浮点数(近似值) ********************    

    /**
     * FLOAT 浮点数 取值范围参照 {@link Float}
     */
    FLOAT,

    /**
     * DOUBLE (同义词 REAL) 浮点数 取值范围参照 {@link Double}
     */
    DOUBLE,

    //**************** 布尔 ************************

    /**
     * BOOLEAN (同义词 BIT)
     */
    BOOLEAN,

    //**************** 浮点数 (精确值) *********************

    /**
     * 精确浮点数 一般对应 {@link BigDecimal}
     */
    DECIMAL,

    //**************** 日期类型 ****************************

    /**
     * 日期类型
     */
    DATE,

    /**
     * 时间类型
     */
    TIME,

    /**
     * 日期和时间类型
     */
    DATETIME,

    //*************** 文本类型 *****************************

    /**
     * 较短的文字 (一般对应 String)
     */
    VARCHAR,

    /**
     * 很长的文字 (用于 VARCHAR 无法存储的大小)
     */
    TEXT,

    /**
     * 非常大的文字 (用于 TEXT 无法存储的大小)
     */
    LONGTEXT,

    /**
     * 二进制数据
     */
    BINARY,

    /**
     * JSON 格式
     */
    JSON;

    private static final Map<Class<?>, StandardDataType> TYPES = initTypes();

    private static Map<Class<?>, StandardDataType> initTypes() {
        var map = new HashMap<Class<?>, StandardDataType>();

        map.put(byte.class, StandardDataType.TINYINT);
        map.put(short.class, StandardDataType.SMALLINT);
        map.put(int.class, StandardDataType.INT);
        map.put(long.class, StandardDataType.BIGINT);
        map.put(float.class, StandardDataType.FLOAT);
        map.put(double.class, StandardDataType.DOUBLE);
        map.put(boolean.class, StandardDataType.BOOLEAN);

        map.put(Byte.class, StandardDataType.TINYINT);
        map.put(Short.class, StandardDataType.SMALLINT);
        map.put(Integer.class, StandardDataType.INT);
        map.put(Long.class, StandardDataType.BIGINT);
        map.put(Double.class, StandardDataType.DOUBLE);
        map.put(Float.class, StandardDataType.FLOAT);
        map.put(Boolean.class, StandardDataType.BOOLEAN);

        map.put(BigInteger.class, StandardDataType.BIGINT);
        map.put(BigDecimal.class, StandardDataType.DECIMAL);

        map.put(LocalDate.class, StandardDataType.DATE);
        map.put(LocalTime.class, StandardDataType.TIME);
        map.put(LocalDateTime.class, StandardDataType.DATETIME);
        map.put(OffsetTime.class, StandardDataType.TIME);
        map.put(OffsetDateTime.class, StandardDataType.DATETIME);
        map.put(ZonedDateTime.class, StandardDataType.DATETIME);

        map.put(Duration.class, StandardDataType.TIME);
        map.put(Instant.class, StandardDataType.DATETIME);

        map.put(String.class, StandardDataType.VARCHAR);

        map.put(byte[].class, StandardDataType.BINARY);
        map.put(Byte[].class, StandardDataType.BINARY);

        return map;
    }

    public static StandardDataType getByJavaType(Class<?> javaType) {
        return TYPES.get(javaType);
    }

}
