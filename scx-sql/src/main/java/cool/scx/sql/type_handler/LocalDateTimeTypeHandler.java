package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, LocalDateTime.class);
    }

}
