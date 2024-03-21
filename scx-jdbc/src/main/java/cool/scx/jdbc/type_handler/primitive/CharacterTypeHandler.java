package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterTypeHandler extends _PrimitiveTypeHandler<Character> {

    public CharacterTypeHandler(boolean isPrimitive) {
        super(isPrimitive, (char) 0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Character parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Character getObject0(ResultSet rs, int index) throws SQLException {
        String columnValue = rs.getString(index);
        if (columnValue == null || columnValue.isEmpty()) {
            return null;
        }
        return columnValue.charAt(0);
    }

}
