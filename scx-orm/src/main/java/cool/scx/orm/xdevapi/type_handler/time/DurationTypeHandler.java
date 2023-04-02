package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;

public class DurationTypeHandler implements TypeHandler<Duration> {

    @Override
    public void setObject(PreparedStatement ps, int i, Duration parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Duration getObject(Row rs, int index) throws SQLException {
        var str = rs.getString(index);
        return str == null ? null : Duration.parse(str);
    }

}
