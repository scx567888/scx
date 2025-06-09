package cool.scx.jdbc.mapping;

import cool.scx.jdbc.JDBCType;

/// DataType
///
/// @author scx567888
/// @version 0.0.1
public interface DataType {

    String name();

    Integer length();

    /// 对应的 JDBCType, 没有返回 null
    default JDBCType jdbcType() {
        return null;
    }

}
