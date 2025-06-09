package cool.scx.jdbc.meta_data;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.mapping.DataType;

/// DataTypeMetaData
///
/// @author scx567888
/// @version 0.0.1
public record DataTypeMetaData(JDBCType jdbcType, String name, Integer length) implements DataType {

}
