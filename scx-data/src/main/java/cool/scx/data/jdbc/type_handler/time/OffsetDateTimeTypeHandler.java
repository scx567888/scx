package cool.scx.data.jdbc.type_handler.time;

import cool.scx.data.jdbc.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.OffsetDateTime;

public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {

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
