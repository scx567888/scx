package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler implements TypeHandler<Short> {

    @Override
    public void setObject(PreparedStatement ps, int i, Short parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter);
    }

    @Override
    public Short getObject(ResultSet rs, int index) throws SQLException {
        short result = rs.getShort(index);
        return result == 0 && rs.wasNull() ? null : result;
    }

}
