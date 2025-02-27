package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.OffsetTime;

/// OffsetTimeTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class OffsetTimeTypeHandler implements TypeHandler<OffsetTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, OffsetTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public OffsetTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, OffsetTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : OffsetTime.parse(str);
        }
    }

}
