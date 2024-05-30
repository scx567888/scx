package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler extends _PrimitiveTypeHandler<Double> {

    public DoubleTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0.0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Double parameter) throws SQLException {
        ps.setDouble(i, parameter);
    }

    @Override
    public Double getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

}
