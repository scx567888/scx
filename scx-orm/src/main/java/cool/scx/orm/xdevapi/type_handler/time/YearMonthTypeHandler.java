package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.YearMonth;

public class YearMonthTypeHandler implements TypeHandler<YearMonth> {

    @Override
    public void setObject(PreparedStatement ps, int i, YearMonth parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public YearMonth getObject(Row rs, int index) throws SQLException {
//        String value = rs.getString(index);
//        return value == null ? null : YearMonth.parse(value);
        return null;
    }

}
