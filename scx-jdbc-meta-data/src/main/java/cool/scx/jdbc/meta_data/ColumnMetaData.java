package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Column;

public record ColumnMetaData(String table,
                             String name,
                             String typeName,
                             Integer columnSize,
                             boolean notNull,
                             boolean autoIncrement,
                             boolean unique,
                             boolean primaryKey,
                             boolean index,
                             String defaultValue,
                             String onUpdateValue,
                             String remarks) implements Column {

}
