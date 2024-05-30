package cool.scx.jdbc.mapping.type;

import cool.scx.common.standard.JDBCType;
import cool.scx.jdbc.mapping.DataType;

public interface TypeDataType extends DataType {

    JDBCType jdbcType();

}
