package cool.scx.data.jdbc.meta_data;

import cool.scx.jdbc.mapping.Key;

public record KeyMetaData(String name, String columnName, boolean primaryKey) implements Key {

}
