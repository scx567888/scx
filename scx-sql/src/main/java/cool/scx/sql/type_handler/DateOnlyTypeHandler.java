package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateOnlyTypeHandler implements TypeHandler<Date> {


    @Override
    public void setObject(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, new java.sql.Date(parameter.getTime()));
    }

    @Override
    public Date getObject(ResultSet rs, int index) throws SQLException {
        java.sql.Date sqlDate = rs.getDate(index);
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }
}
