package cool.scx.sql.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntTypeHandler extends _PrimitiveTypeHandler<Integer> {

    public IntTypeHandler(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Integer parameter) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getPrimitiveObject(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

}
