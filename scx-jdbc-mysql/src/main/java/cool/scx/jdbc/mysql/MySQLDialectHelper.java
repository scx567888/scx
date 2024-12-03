package cool.scx.jdbc.mysql;

import com.mysql.cj.MysqlType;
import cool.scx.jdbc.JDBCType;

import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * MySQLDialectHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
final class MySQLDialectHelper {

    private final static Map<String, JDBCType> MAP = initMAP();

    private static Map<String, JDBCType> initMAP() {
        var map = new TreeMap<String, JDBCType>(CASE_INSENSITIVE_ORDER);

        map.put("TINYINT", JDBCType.TINYINT);
        map.put("TINYINT UNSIGNED", JDBCType.TINYINT);
        map.put("SMALLINT", JDBCType.SMALLINT);
        map.put("SMALLINT UNSIGNED", JDBCType.SMALLINT);
        map.put("MEDIUMINT UNSIGNED", JDBCType.INT);
        map.put("INT", JDBCType.INT);
        map.put("INT UNSIGNED", JDBCType.INT);
        map.put("BIGINT", JDBCType.BIGINT);
        map.put("BIGINT UNSIGNED", JDBCType.BIGINT);

        map.put("FLOAT", JDBCType.FLOAT);
        map.put("DOUBLE", JDBCType.DOUBLE);
        map.put("DECIMAL", JDBCType.DECIMAL);
        map.put("DECIMAL UNSIGNED", JDBCType.DECIMAL);

        map.put("BIT", JDBCType.BOOLEAN);

        map.put("TIME", JDBCType.TIME);
        map.put("DATE", JDBCType.DATE);
        map.put("DATETIME", JDBCType.DATETIME);
        map.put("TIMESTAMP", JDBCType.DATETIME);

        map.put("VARCHAR", JDBCType.VARCHAR);
        map.put("CHAR", JDBCType.VARCHAR);

        map.put("TEXT", JDBCType.TEXT);
        map.put("MEDIUMTEXT", JDBCType.LONGTEXT);
        map.put("LONGTEXT", JDBCType.LONGTEXT);

        map.put("BLOB", JDBCType.BLOB);
        map.put("MEDIUMBLOB", JDBCType.LONGBLOB);
        map.put("LONGBLOB", JDBCType.LONGBLOB);

        map.put("BINARY", JDBCType.BLOB);
        map.put("VARBINARY", JDBCType.BLOB);

        map.put("JSON", JDBCType.JSON);

        map.put("ENUM", JDBCType.VARCHAR);
        map.put("SET", JDBCType.VARCHAR);

        return map;
    }

    public static JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        var standardDataType = MAP.get(dialectDataType);
        if (standardDataType == null) {
            throw new IllegalArgumentException("未知方言数据类型 : " + dialectDataType);
        }
        return standardDataType;
    }

    public static MysqlType jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return switch (jdbcType) {
            case TINYINT -> MysqlType.TINYINT;
            case SMALLINT -> MysqlType.SMALLINT;
            case INT -> MysqlType.INT;
            case BIGINT -> MysqlType.BIGINT;
            case FLOAT -> MysqlType.FLOAT;
            case DOUBLE -> MysqlType.DOUBLE;
            case BOOLEAN -> MysqlType.BOOLEAN;
            case DECIMAL -> MysqlType.DECIMAL;
            case DATE -> MysqlType.DATE;
            case TIME -> MysqlType.TIME;
            case DATETIME -> MysqlType.DATETIME;
            case VARCHAR -> MysqlType.VARCHAR;
            case TEXT -> MysqlType.TEXT;
            case LONGTEXT -> MysqlType.LONGTEXT;
            case BLOB -> MysqlType.BLOB;
            case LONGBLOB -> MysqlType.LONGBLOB;
            case JSON -> MysqlType.JSON;
        };
    }

}
