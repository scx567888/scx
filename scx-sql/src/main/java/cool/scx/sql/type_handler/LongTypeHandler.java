package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements TypeHandler<Long> {

    @Override
    public void setObject(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getObject(ResultSet rs, int index) throws SQLException {
        long result = rs.getLong(index);
        return result == 0 && rs.wasNull() ? null : result;
    }

}
