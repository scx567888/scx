package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.ZonedDateTime;

/// ZonedDateTimeTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class ZonedDateTimeTypeHandler implements TypeHandler<ZonedDateTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, ZonedDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public ZonedDateTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, ZonedDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : ZonedDateTime.parse(str);
        }
    }

}
