package cool.scx.sql.meta_data;

import cool.scx.sql.mapping.Index;

public record IndexMetaData(String name, String columnName, boolean unique) implements Index {

}
