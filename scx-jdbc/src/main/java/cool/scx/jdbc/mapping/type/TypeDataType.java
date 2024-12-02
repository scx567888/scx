package cool.scx.jdbc.mapping.type;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.mapping.DataType;

/**
 * 具有 java 类型 的 DataType
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface TypeDataType extends DataType {

    JDBCType jdbcType();

}
