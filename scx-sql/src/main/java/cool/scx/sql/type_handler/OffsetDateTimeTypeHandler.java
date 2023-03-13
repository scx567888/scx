package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

//todo test
public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {

    @Override
    public OffsetDateTime getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, OffsetDateTime.class);
    }

}
