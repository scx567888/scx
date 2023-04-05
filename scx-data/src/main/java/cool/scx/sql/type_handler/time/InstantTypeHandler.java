package cool.scx.sql.type_handler.time;

import cool.scx.sql.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.Instant;


public class InstantTypeHandler implements TypeHandler<Instant> {

    @Override
    public Instant getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, Instant.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : Instant.parse(str);
        }
    }

}
