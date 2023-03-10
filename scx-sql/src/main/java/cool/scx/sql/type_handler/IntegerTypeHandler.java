package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler<Integer> {

    @Override
    public void setObject(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getObject(ResultSet rs, int index) throws SQLException {
        int result = rs.getInt(index);
        return result == 0 && rs.wasNull() ? null : result;
    }

}
