package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

/// YearTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class YearTypeHandler implements TypeHandler<Year> {

    @Override
    public void setObject(PreparedStatement ps, int i, Year parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Year getObject(ResultSet rs, int index) throws SQLException {
        int year = rs.getInt(index);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }

}
