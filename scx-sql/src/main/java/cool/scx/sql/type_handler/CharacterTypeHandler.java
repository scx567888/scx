package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterTypeHandler implements TypeHandler<Character> {

    @Override
    public void setObject(PreparedStatement ps, int i, Character parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Character getObject(ResultSet rs, int index) throws SQLException {
        String columnValue = rs.getString(index);
        if (columnValue == null || columnValue.isEmpty()) {
            return null;
        }
        return columnValue.charAt(0);
    }

}
