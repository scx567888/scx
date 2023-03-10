package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class TimeOnlyTypeHandler implements TypeHandler<Date> {

    @Override
    public void setObject(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(i, new Time(parameter.getTime()));
    }

    @Override
    public Date getObject(ResultSet rs, int index) throws SQLException {
        Time sqlTime = rs.getTime(index);
        if (sqlTime == null) {
            return null;
        }
        return new Date(sqlTime.getTime());
    }

}
