package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.DataType;

public record DataTypeMetaData(String name, Integer length) implements DataType {

}
