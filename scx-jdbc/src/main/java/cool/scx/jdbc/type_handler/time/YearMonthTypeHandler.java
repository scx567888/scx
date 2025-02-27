package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

/// YearMonthTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class YearMonthTypeHandler implements TypeHandler<YearMonth> {

    @Override
    public void setObject(PreparedStatement ps, int i, YearMonth parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public YearMonth getObject(ResultSet rs, int index) throws SQLException {
        String value = rs.getString(index);
        return value == null ? null : YearMonth.parse(value);
    }

}
