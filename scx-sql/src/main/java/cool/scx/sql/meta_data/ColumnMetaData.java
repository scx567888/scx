package cool.scx.sql.meta_data;

import cool.scx.sql.MetaDataHelper._Column;
import cool.scx.sql.mapping.ColumnMapping;

public record ColumnMetaData(String tableName, String columnName, String typeName, Integer columnSize,
                             Boolean isNullable, Boolean isAutoincrement, String remarks,
                             _Column _column) implements ColumnMapping {

}
