package cool.scx.data.jdbc.meta_data;

import cool.scx.jdbc.mapping.Index;

public record IndexMetaData(String name, String columnName, boolean unique) implements Index {

}
