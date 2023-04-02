package cool.scx.orm.xdevapi.type_handler.primitive;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
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
    public Double getObject0(Row rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

}
