package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.Column;

public record ColumnMetaData(String tableName, String columnName, String typeName, Integer columnSize,
                             Boolean isNullable, Boolean isAutoincrement, String remarks,
                             SchemaHelper._Column _column) implements Column {

}
