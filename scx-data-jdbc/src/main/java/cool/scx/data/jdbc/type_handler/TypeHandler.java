package cool.scx.data.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    default void setObject(PreparedStatement ps, int i, T parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    T getObject(ResultSet rs, int index) throws SQLException;

    /**
     * 类型的默认值
     *
     * @return nullValue
     */
    default T getDefaultValue() {
        return null;
    }

}
