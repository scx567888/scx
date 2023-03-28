package cool.scx.sql.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    default void setObject(PreparedStatement ps, int i, T parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    T getObject(ResultSet rs, int index) throws SQLException;

    /**
     * primary 值需要返回 非空值
     *
     * @return nullValue
     */
    default T getNullValue() {
        return null;
    }

}
