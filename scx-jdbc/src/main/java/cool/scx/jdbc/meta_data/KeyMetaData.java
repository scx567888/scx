package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Key;

public record KeyMetaData(String name, String columnName, boolean primary) implements Key {

}
