package cool.scx.orm.jdbc.meta_data;

import cool.scx.orm.jdbc.mapping.Index;

public record IndexMetaData(String name, String columnName, boolean unique) implements Index {

}
