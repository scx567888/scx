package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.mapping.DataType;

/**
 * DataTypeMetaData
 *
 * @author scx567888
 * @version 0.0.1
 */
public record DataTypeMetaData(String name, Integer length) implements DataType {

}
