package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Month;

//todo test
public class MonthTypeHandler implements TypeHandler<Month> {

    @Override
    public void setObject(PreparedStatement ps, int i, Month parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Month getObject(Row rs, int index) throws SQLException {
        int month = rs.getInt(index);
//        return month == 0 && rs.wasNull() ? null : Month.of(month);
        return Month.of(month);
    }

}
