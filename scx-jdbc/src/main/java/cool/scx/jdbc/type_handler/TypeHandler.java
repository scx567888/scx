package cool.scx.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// TypeHandler
///
/// @param <T> T
/// @author scx567888
/// @version 0.0.1
public interface TypeHandler<T> {

    void setObject(PreparedStatement ps, int i, T parameter) throws SQLException;

    T getObject(ResultSet rs, int index) throws SQLException;

    /// 类型的默认值
    ///
    /// @return nullValue
    default T getDefaultValue() {
        return null;
    }

}
