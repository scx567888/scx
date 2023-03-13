package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

//todo test
public class MonthTypeHandler implements TypeHandler<Month> {

    @Override
    public void setObject(PreparedStatement ps, int i, Month parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Month getObject(ResultSet rs, int index) throws SQLException {
        int month = rs.getInt(index);
        return month == 0 && rs.wasNull() ? null : Month.of(month);
    }

}
