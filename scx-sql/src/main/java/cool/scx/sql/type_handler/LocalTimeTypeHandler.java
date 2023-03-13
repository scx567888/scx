package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

//todo test
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public LocalTime getObject(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalTime.class);
    }

}
