package cool.scx.dao.schema;

import java.util.Objects;

public record ColumnMetaData(String tableName, String columnName, String typeName, Integer columnSize,
                             Boolean isNullable, Boolean isAutoincrement, String remarks,
                             _Column _column) {

    public static ColumnMetaData of(_Column _column) {
        var tabledName = _column.TABLE_NAME();
        var columnName = _column.COLUMN_NAME();
        var typeName = _column.TYPE_NAME();
        var columnSize = _column.COLUMN_SIZE();
        var remarks = _column.REMARKS();
        var isNullable = Objects.equals("YES", _column.IS_NULLABLE());
        var isAutoincrement = Objects.equals("YES", _column.IS_AUTOINCREMENT());
        return new ColumnMetaData(tabledName, columnName, typeName, columnSize, isNullable, isAutoincrement, remarks, _column);
    }

    public record _Column(String SCOPE_TABLE, String TABLE_CAT, Integer BUFFER_LENGTH, String IS_NULLABLE,
                          String TABLE_NAME, String COLUMN_DEF, String SCOPE_CATALOG, String TABLE_SCHEM,
                          String COLUMN_NAME, Integer NULLABLE, String REMARKS, Integer DECIMAL_DIGITS,
                          Integer NUM_PREC_RADIX, Integer SQL_DATETIME_SUB, String IS_GENERATEDCOLUMN,
                          String IS_AUTOINCREMENT, Integer SQL_DATA_TYPE, Integer CHAR_OCTET_LENGTH,
                          Integer ORDINAL_POSITION, String SCOPE_SCHEMA, String SOURCE_DATA_TYPE, Integer DATA_TYPE,
                          String TYPE_NAME, Integer COLUMN_SIZE) {

    }

}
