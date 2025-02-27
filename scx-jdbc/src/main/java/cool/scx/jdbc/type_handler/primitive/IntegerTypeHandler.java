package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// IntegerTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class IntegerTypeHandler extends PrimitiveTypeHandler<Integer> {

    public IntegerTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Integer parameter) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

}
