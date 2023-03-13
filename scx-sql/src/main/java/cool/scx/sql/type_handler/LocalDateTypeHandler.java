package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

//todo test
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    @Override
    public LocalDate getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, LocalDate.class);
    }
}
