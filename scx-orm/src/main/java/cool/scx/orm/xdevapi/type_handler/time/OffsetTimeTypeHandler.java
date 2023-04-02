package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.OffsetTime;

public class OffsetTimeTypeHandler implements TypeHandler<OffsetTime> {

    @Override
    public OffsetTime getObject(Row rs, int index) throws SQLException {
//        try {
//            return rs.getObject(index, OffsetTime.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(index);
//            return str == null ? null : OffsetTime.parse(str);
//        }
        return null;
    }

}
