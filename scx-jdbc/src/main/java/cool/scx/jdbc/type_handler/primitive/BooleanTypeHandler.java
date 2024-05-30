package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler extends _PrimitiveTypeHandler<Boolean> {

    public BooleanTypeHandler(boolean isPrimitive) {
        super(isPrimitive, false);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Boolean parameter) throws SQLException {
        ps.setBoolean(i, parameter);
    }

    @Override
    public Boolean getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getBoolean(index);
    }

}
