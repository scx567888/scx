package cool.scx.sql.type_handler.time;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.OffsetTime;

public class OffsetTimeTypeHandler implements TypeHandler<OffsetTime> {

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
