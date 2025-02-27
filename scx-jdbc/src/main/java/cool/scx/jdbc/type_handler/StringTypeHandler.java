package cool.scx.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// StringTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class StringTypeHandler implements TypeHandler<String> {

    @Override
    public void setObject(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getObject(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }

}
