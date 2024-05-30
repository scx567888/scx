package cool.scx.jdbc.sqlite;

import cool.scx.common.standard.JDBCType;

public class SQLiteDialectHelper {

    public static JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        if ("TEXT".equalsIgnoreCase(dialectDataType)) {
            return JDBCType.VARCHAR;
        } else if ("INTEGER".equalsIgnoreCase(dialectDataType)) {
            return JDBCType.INT;
        } else {
            throw new IllegalArgumentException("未知方言数据类型 : " + dialectDataType);
        }
    }

    public static String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return switch (jdbcType) {
            case TINYINT, SMALLINT, INT, BIGINT, BOOLEAN -> "INTEGER";
            case FLOAT, DOUBLE, DECIMAL -> "REAL";
            case DATE, TIME, DATETIME, VARCHAR, TEXT, LONGTEXT, JSON -> "TEXT";
            case BLOB, LONGBLOB -> "BLOB";
        };
    }

}
