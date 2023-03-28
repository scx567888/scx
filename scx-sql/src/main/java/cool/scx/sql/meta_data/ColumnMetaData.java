package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.ColumnMapping;
import cool.scx.sql.meta_data.MetaDataHelper._Column;

public record ColumnMetaData(String tableName, String columnName, String typeName, Integer columnSize,
                             boolean notNull, boolean autoIncrement,
                             boolean unique,
                             String defaultValue,
                             String onUpdateValue,
                             String remarks,
                             _Column _column) implements ColumnMapping {

}
