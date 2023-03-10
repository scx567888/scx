package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class ZonedDateTimeTypeHandler implements TypeHandler<ZonedDateTime> {

    @Override
    public ZonedDateTime getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, ZonedDateTime.class);
    }

}
