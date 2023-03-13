package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantTypeHandler implements TypeHandler<Instant> {

    private static Instant getInstant(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toInstant();
        }
        return null;
    }


    @Override
    public void setObject(PreparedStatement ps, int i, Instant parameter) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter));
    }

    @Override
    public Instant getObject(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(index);
        return getInstant(timestamp);
    }

}
