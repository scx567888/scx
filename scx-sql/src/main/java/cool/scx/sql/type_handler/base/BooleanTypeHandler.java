package cool.scx.sql.type_handler.base;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public void setObject(PreparedStatement ps, int i, Boolean parameter) throws SQLException {
        ps.setBoolean(i, parameter);
    }

    @Override
    public Boolean getObject(ResultSet rs, int index) throws SQLException {
        boolean result = rs.getBoolean(index);
        return rs.wasNull() ? null : result;
    }

}
