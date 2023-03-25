package cool.scx.sql.schema.impl;

import cool.scx.sql.SchemaHelper;
import cool.scx.sql.schema.PrimaryKey;

public record PrimaryKeyMetaData(String columnName, SchemaHelper._PrimaryKey _primaryKey) implements PrimaryKey {

}
