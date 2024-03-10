package cool.scx.jdbc.mysql;

import com.mysql.cj.MysqlType;
import cool.scx.jdbc.standard.StandardDataType;

public final class MySQLDialectHelper {

    public static StandardDataType dialectDataTypeToStandardDataType(String dialectDataType) {
        if ("VARCHAR".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.VARCHAR;
        } else if ("BIT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BOOLEAN;
        } else if ("INT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DATETIME;
        } else if ("BIGINT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BIGINT;
        } else if ("BIGINT UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BIGINT;
        } else if ("TEXT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.TEXT;
        } else if ("DATETIME".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DATETIME;
        } else if ("JSON".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.JSON;
        } else if ("DATE".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DATE;
        } else if ("INT UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.INT;
        } else if ("LONGTEXT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.LONGTEXT;
        } else if ("ENUM".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.VARCHAR;
        } else if ("MEDIUMTEXT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.TEXT;
        } else if ("SET".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.VARCHAR;
        } else if ("TIMESTAMP".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DATETIME;
        } else if ("CHAR".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.VARCHAR;
        } else if ("BINARY".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BINARY;
        } else if ("FLOAT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.FLOAT;
        } else if ("VARBINARY".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BINARY;
        } else if ("DECIMAL".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DECIMAL;
        } else if ("BLOB".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BINARY;
        } else if ("DOUBLE".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DOUBLE;
        } else if ("TINYINT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.TINYINT;
        } else if ("MEDIUMBLOB".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BINARY;
        } else if ("SMALLINT UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.SMALLINT;
        } else if ("TINYINT UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.TINYINT;
        } else if ("TIME".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.TIME;
        } else if ("SMALLINT".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.SMALLINT;
        } else if ("MEDIUMINT UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.INT;
        } else if ("LONGBLOB".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.BINARY;
        } else if ("DECIMAL UNSIGNED".equalsIgnoreCase(dialectDataType)) {
            return StandardDataType.DECIMAL;
        } else {
            throw new IllegalArgumentException("未知方言数据类型 : " + dialectDataType);
        }
    }

    public static MysqlType standardDataTypeToDialectDataType(StandardDataType standardDataType) {
        return switch (standardDataType) {
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
            case BINARY -> MysqlType.BINARY;
            case JSON -> MysqlType.JSON;
        };
    }

}
