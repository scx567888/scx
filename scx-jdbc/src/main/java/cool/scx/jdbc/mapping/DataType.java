package cool.scx.jdbc.mapping;

import cool.scx.jdbc.JDBCType;

/// 数据类型
///
/// @author scx567888
/// @version 0.0.1
public interface DataType {

    JDBCType jdbcType();

    String name();

    Integer length();

}
