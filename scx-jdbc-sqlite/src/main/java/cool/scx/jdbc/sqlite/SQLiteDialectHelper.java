package cool.scx.jdbc.sqlite;

import cool.scx.jdbc.standard.StandardDataType;

public class SQLiteDialectHelper {

    public static StandardDataType dialectDataTypeToStandardDataType(String dialectDataType) {
        if ("TEXT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.VARCHAR;
        } else if ("INTEGER".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.INT;
        } else {
            throw new IllegalArgumentException("未知方言数据类型 : " + dialectDataType);
        }
    }

    public static String standardDataTypeToDialectDataType(StandardDataType standardDataType) {
        return switch (standardDataType) {
            case TINYINT, SMALLINT, INT, BIGINT, BOOLEAN -> "INTEGER";
            case FLOAT, DOUBLE, DECIMAL -> "REAL";
            case DATE, TIME, DATETIME, VARCHAR, TEXT, LONGTEXT, JSON -> "TEXT";
            case BINARY -> "BLOB";
        };
    }

}
