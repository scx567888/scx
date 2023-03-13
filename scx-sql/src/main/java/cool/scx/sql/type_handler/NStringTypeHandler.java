package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NStringTypeHandler implements TypeHandler<String> {


    @Override
    public void setObject(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setNString(i, parameter);
    }

    @Override
    public String getObject(ResultSet rs, int index) throws SQLException {
        return rs.getNString(index);
    }
}
