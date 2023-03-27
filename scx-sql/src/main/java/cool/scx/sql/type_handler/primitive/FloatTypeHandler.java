package cool.scx.sql.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler extends _PrimitiveTypeHandler<Float> {

    public FloatTypeHandler(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getPrimitiveObject(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

}
