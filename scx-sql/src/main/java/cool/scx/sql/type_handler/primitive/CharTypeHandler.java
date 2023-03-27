package cool.scx.sql.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharTypeHandler extends _PrimitiveTypeHandler<Character> {

    public CharTypeHandler(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Character parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Character getPrimitiveObject(ResultSet rs, int index) throws SQLException {
        var columnValue = rs.getString(index);
        return columnValue == null || columnValue.isEmpty() ? 0 : columnValue.charAt(0);
    }

}
