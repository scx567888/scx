package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Year;

public class YearTypeHandler implements TypeHandler<Year> {

    @Override
    public void setObject(PreparedStatement ps, int i, Year parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Year getObject(Row rs, int index) throws SQLException {
//        int year = rs.getInt(index);
//        return year == 0 && rs.wasNull() ? null : Year.of(year);
        return null;
    }

}
