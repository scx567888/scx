package cool.scx.jdbc.mapping.type;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.mapping.DataType;

public interface TypeDataType extends DataType {

    JDBCType jdbcType();

}
