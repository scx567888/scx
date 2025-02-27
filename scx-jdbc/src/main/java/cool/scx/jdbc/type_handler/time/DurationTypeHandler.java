package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

/// DurationTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class DurationTypeHandler implements TypeHandler<Duration> {

    @Override
    public void setObject(PreparedStatement ps, int i, Duration parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Duration getObject(ResultSet rs, int index) throws SQLException {
        var str = rs.getString(index);
        return str == null ? null : Duration.parse(str);
    }

}
