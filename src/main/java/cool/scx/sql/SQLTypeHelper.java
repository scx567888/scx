package cool.scx.sql;

import com.mysql.cj.MysqlType;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建 SQL 的助手(常用方法) 类
 *
 * @author scx567888
 * @version 1.1.0
 */
public final class SQLTypeHelper {

    /**
     * 这里是直接从 mysql 驱动中复制出来的
     */
    private static final Map<Class<?>, MysqlType> DEFAULT_MYSQL_TYPES = new HashMap<>();

    static {

        //这里 我们在额外添加几个下表对应的基本类型或包装类型
        DEFAULT_MYSQL_TYPES.put(byte.class, MysqlType.INT);
        DEFAULT_MYSQL_TYPES.put(short.class, MysqlType.SMALLINT);
        DEFAULT_MYSQL_TYPES.put(int.class, MysqlType.INT);
        DEFAULT_MYSQL_TYPES.put(long.class, MysqlType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(float.class, MysqlType.FLOAT);
        DEFAULT_MYSQL_TYPES.put(double.class, MysqlType.DOUBLE);
        DEFAULT_MYSQL_TYPES.put(Byte[].class, MysqlType.BINARY);
        DEFAULT_MYSQL_TYPES.put(boolean.class, MysqlType.BOOLEAN);

        //这里是从 mysql 中直接复制出来的
        DEFAULT_MYSQL_TYPES.put(String.class, MysqlType.VARCHAR);
        DEFAULT_MYSQL_TYPES.put(java.sql.Date.class, MysqlType.DATE);
        DEFAULT_MYSQL_TYPES.put(java.sql.Time.class, MysqlType.TIME);
        DEFAULT_MYSQL_TYPES.put(java.sql.Timestamp.class, MysqlType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(Byte.class, MysqlType.INT);
        DEFAULT_MYSQL_TYPES.put(BigDecimal.class, MysqlType.DECIMAL);
        DEFAULT_MYSQL_TYPES.put(Short.class, MysqlType.SMALLINT);
        DEFAULT_MYSQL_TYPES.put(Integer.class, MysqlType.INT);
        DEFAULT_MYSQL_TYPES.put(Long.class, MysqlType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(Float.class, MysqlType.FLOAT); // TODO check; was Types.FLOAT but should be Types.REAL to map to SQL FLOAT
        DEFAULT_MYSQL_TYPES.put(Double.class, MysqlType.DOUBLE);
        DEFAULT_MYSQL_TYPES.put(byte[].class, MysqlType.BINARY);
        DEFAULT_MYSQL_TYPES.put(Boolean.class, MysqlType.BOOLEAN);
        DEFAULT_MYSQL_TYPES.put(LocalDate.class, MysqlType.DATE);
        DEFAULT_MYSQL_TYPES.put(LocalTime.class, MysqlType.TIME);
        DEFAULT_MYSQL_TYPES.put(LocalDateTime.class, MysqlType.DATETIME); // default JDBC mapping is TIMESTAMP, see B-4
        DEFAULT_MYSQL_TYPES.put(OffsetTime.class, MysqlType.TIME); // default JDBC mapping is TIME_WITH_TIMEZONE, see B-4
        DEFAULT_MYSQL_TYPES.put(OffsetDateTime.class, MysqlType.TIMESTAMP); // default JDBC mapping is TIMESTAMP_WITH_TIMEZONE, see B-4
        DEFAULT_MYSQL_TYPES.put(ZonedDateTime.class, MysqlType.TIMESTAMP); // no JDBC mapping is defined
        DEFAULT_MYSQL_TYPES.put(Duration.class, MysqlType.TIME);
        DEFAULT_MYSQL_TYPES.put(java.sql.Blob.class, MysqlType.BLOB);
        DEFAULT_MYSQL_TYPES.put(java.sql.Clob.class, MysqlType.TEXT);
        DEFAULT_MYSQL_TYPES.put(BigInteger.class, MysqlType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(java.util.Date.class, MysqlType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(java.util.Calendar.class, MysqlType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(InputStream.class, MysqlType.BLOB);
    }

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link java.lang.String} object.
     */
    public static String getMySQLTypeCreateName(Class<?> javaType) {
        var mysqlType = getMySQLType(javaType);
        if (mysqlType == null) {
            if (javaType.isEnum()) {
                mysqlType = MysqlType.VARCHAR;
            } else {
                mysqlType = MysqlType.JSON;
            }
        }
        return mysqlType == MysqlType.VARCHAR ? mysqlType.getName() + "(128)" : mysqlType.getName();
    }

    /**
     * 获取 mysql 类型
     * 用于后续判断类型是否可以由 JDBC 进行 SQLType 到 JavaType 的直接转换
     * <p>
     * 例子 :
     * String 可以由 varchar 直接转换 true
     * Integer 可以由 int 直接转换 true
     * User 不可以由 json 直接转换 false
     *
     * @param javaType 需要判断的类型
     * @return r
     */
    public static MysqlType getMySQLType(Class<?> javaType) {
        var mysqlType = DEFAULT_MYSQL_TYPES.get(javaType);
        if (mysqlType == null) {
            return DEFAULT_MYSQL_TYPES.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(javaType))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null);
        }
        return mysqlType;
    }

}
