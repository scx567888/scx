package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.Key;

public record KeyMetaData(String name, String columnName, boolean primaryKey) implements Key {

}
