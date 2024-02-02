package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.Index;

public record IndexMetaData(String name, String columnName, boolean unique) implements Index {

}
