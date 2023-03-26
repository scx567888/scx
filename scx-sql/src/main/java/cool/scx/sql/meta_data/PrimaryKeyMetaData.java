package cool.scx.sql.meta_data;

import cool.scx.sql.MetaDataHelper._PrimaryKey;
import cool.scx.sql.mapping.PrimaryKeyMapping;

public record PrimaryKeyMetaData(String columnName, _PrimaryKey _primaryKey) implements PrimaryKeyMapping {

}
