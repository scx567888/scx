package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.ZonedDateTime;

public class ZonedDateTimeTypeHandler implements TypeHandler<ZonedDateTime> {

    @Override
    public ZonedDateTime getObject(Row rs, int index) throws SQLException {
//        try {
//            return rs.getObject(index, ZonedDateTime.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(index);
//            return str == null ? null : ZonedDateTime.parse(str);
//        }
        return null;
    }

}
