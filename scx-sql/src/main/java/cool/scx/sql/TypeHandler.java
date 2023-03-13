package cool.scx.sql;

import cool.scx.sql.type_handler.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    default void setObject(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    T getObject(ResultSet rs, int index) throws SQLException;

}
