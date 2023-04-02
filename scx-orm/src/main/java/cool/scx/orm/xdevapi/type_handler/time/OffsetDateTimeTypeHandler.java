package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {

    @Override
    public OffsetDateTime getObject(Row rs, int index) throws SQLException {
//        try {
//            return rs.getObject(index, OffsetDateTime.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(index);
//            return str == null ? null : OffsetDateTime.parse(str);
//        }
        return null;
    }

}
