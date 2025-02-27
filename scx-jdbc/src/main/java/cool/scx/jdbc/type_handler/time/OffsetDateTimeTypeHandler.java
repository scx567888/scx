package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.OffsetDateTime;

/// OffsetDateTimeTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, OffsetDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public OffsetDateTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, OffsetDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : OffsetDateTime.parse(str);
        }
    }

}
