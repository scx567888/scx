package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.PrimaryKeyMapping;
import cool.scx.sql.meta_data.MetaDataHelper._PrimaryKey;

public record PrimaryKeyMetaData(String columnName, _PrimaryKey _primaryKey) implements PrimaryKeyMapping {

}
