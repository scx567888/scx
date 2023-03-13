package cool.scx.sql.type_handler.base;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements TypeHandler<Long> {

    @Override
    public void setObject(PreparedStatement ps, int i, Long parameter) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getObject(ResultSet rs, int index) throws SQLException {
        long result = rs.getLong(index);
        return rs.wasNull() ? null : result;
    }

}
