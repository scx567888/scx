package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

/// MonthTypeHandler
/// todo test
///
/// @author scx567888
/// @version 0.0.1
public class MonthTypeHandler implements TypeHandler<Month> {

    @Override
    public void setObject(PreparedStatement ps, int i, Month parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Month getObject(ResultSet rs, int index) throws SQLException {
        int month = rs.getInt(index);
        return month == 0 && rs.wasNull() ? null : Month.of(month);
    }

}
