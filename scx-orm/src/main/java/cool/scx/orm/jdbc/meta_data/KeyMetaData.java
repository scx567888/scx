package cool.scx.orm.jdbc.meta_data;

import cool.scx.orm.jdbc.mapping.Key;

public record KeyMetaData(String name, String columnName, boolean primaryKey) implements Key {

}
