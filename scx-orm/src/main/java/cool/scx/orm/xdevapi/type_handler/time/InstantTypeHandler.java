package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.Instant;


public class InstantTypeHandler implements TypeHandler<Instant> {

    @Override
    public Instant getObject(Row rs, int index) throws SQLException {
//        try {
//            return rs.getObject(index, Instant.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(index);
//            return str == null ? null : Instant.parse(str);
//        }
        return null;
    }

}
