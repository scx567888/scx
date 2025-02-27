package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// FloatTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class FloatTypeHandler extends PrimitiveTypeHandler<Float> {

    public FloatTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0F);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

}
