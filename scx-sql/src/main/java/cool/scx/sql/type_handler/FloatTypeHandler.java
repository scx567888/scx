package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler implements TypeHandler<Float> {

    @Override
    public void setObject(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getObject(ResultSet rs, int index) throws SQLException {
        float result = rs.getFloat(index);
        return result == 0 && rs.wasNull() ? null : result;
    }

}
