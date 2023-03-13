package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler implements TypeHandler<Double> {

    @Override
    public void setObject(PreparedStatement ps, int i, Double parameter) throws SQLException {
        ps.setDouble(i, parameter);
    }

    @Override
    public Double getObject(ResultSet rs, int index) throws SQLException {
        double result = rs.getDouble(index);
        return rs.wasNull() ? null : result;
    }

}
