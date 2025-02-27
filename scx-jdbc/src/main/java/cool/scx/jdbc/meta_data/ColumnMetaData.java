package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Column;

/// ColumnMetaData
///
/// @author scx567888
/// @version 0.0.1
public record ColumnMetaData(String table,
                             String name,
                             DataTypeMetaData dataType,
                             String defaultValue,
                             String onUpdate,
                             boolean notNull,
                             boolean autoIncrement,
                             boolean primary,
                             boolean unique,
                             boolean index,
                             String comment) implements Column {

}
