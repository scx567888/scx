package cool.scx.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

/// JDBCType
///
/// @author scx567888
/// @version 0.0.1
public enum JDBCType {

    //***************** 整数 ********************

    /// 微小的整数类型 取值范围参照 [Byte]
    TINYINT,

    /// 较小的整数类型 取值范围参照 [Short]
    SMALLINT,

    /// 整数类型 取值范围参照 [Integer]
    INT,

    /// 非常大的整数类型 取值范围参照 [Long]
    BIGINT,

    //**************** 浮点数 (近似值) ********************    

    /// FLOAT (同义词 REAL) 浮点数 取值范围参照 [Float]
    FLOAT,

    /// DOUBLE 浮点数 取值范围参照 [Double]
    DOUBLE,

    //**************** 布尔 ************************

    /// BOOLEAN (同义词 BIT) 一般对应 [Boolean]
    BOOLEAN,

    //**************** 浮点数 (精确值) *********************

    /// 精确浮点数 (同义词 NUMERIC) 一般对应 [BigDecimal] 或 [BigInteger]
    DECIMAL,

    //**************** 日期和时间 **********************

    /// 日期类型  一般对应 [LocalDate]
    DATE,

    /// 时间类型 一般对应 [LocalTime]
    TIME,

    /// 日期和时间类型 一般对应 [LocalDateTime]
    DATETIME,

    //***************** 文本类型 *********************

    /// 较短的文字 一般对应 [String]
    VARCHAR,

    //**************** TEXT 类型 ********************

    /// 很长的文字 (用于 VARCHAR 无法存储的大小)
    TEXT,

    /// 非常大的文字 (用于 TEXT 无法存储的大小)
    LONGTEXT,

    //***************** BLOB 类型 ********************

    /// 二进制数据  一般用于存储 byte 数组
    BLOB,

    /// 非常大的二进制数据 (用于 BLOB 无法存储的大小)
    LONGBLOB,

    //**************** 其余类型 *********************

    /// JSON 格式
    JSON;

    private static final Map<Class<?>, JDBCType> MAP = initMap();

    private static Map<Class<?>, JDBCType> initMap() {
        var map = new HashMap<Class<?>, JDBCType>();

        map.put(byte.class, TINYINT);
        map.put(short.class, SMALLINT);
        map.put(int.class, INT);
        map.put(long.class, BIGINT);
        map.put(float.class, FLOAT);
        map.put(double.class, DOUBLE);
        map.put(boolean.class, BOOLEAN);

        map.put(Byte.class, TINYINT);
        map.put(Short.class, SMALLINT);
        map.put(Integer.class, INT);
        map.put(Long.class, BIGINT);
        map.put(Float.class, FLOAT);
        map.put(Double.class, DOUBLE);
        map.put(Boolean.class, BOOLEAN);

        map.put(BigInteger.class, BIGINT);
        map.put(BigDecimal.class, DECIMAL);

        map.put(LocalDate.class, DATE);
        map.put(LocalTime.class, TIME);
        map.put(LocalDateTime.class, DATETIME);
        map.put(OffsetTime.class, TIME);
        map.put(OffsetDateTime.class, DATETIME);
        map.put(ZonedDateTime.class, DATETIME);

        map.put(Duration.class, TIME);
        map.put(Instant.class, DATETIME);

        map.put(String.class, VARCHAR);

        map.put(byte[].class, BLOB);
        map.put(Byte[].class, BLOB);

        return map;
    }

    public static JDBCType getByJavaType(Class<?> javaType) {
        return MAP.get(javaType);
    }

}
