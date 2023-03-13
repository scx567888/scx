package cool.scx.sql.type_handler.base;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler implements TypeHandler<Short> {

    @Override
    public void setObject(PreparedStatement ps, int i, Short parameter) throws SQLException {
        ps.setShort(i, parameter);
    }

    @Override
    public Short getObject(ResultSet rs, int index) throws SQLException {
        short result = rs.getShort(index);
        return rs.wasNull() ? null : result;
    }

}
