package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.LocalTime;

/**
 * 为不支持 LocalTime 的数据库添加 string 类型的兼容支持
 */
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public LocalTime getObject(Row rs, int columnIndex) throws SQLException {
//        try {
//            return rs.getObject(columnIndex, LocalTime.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(columnIndex);
//            return str == null ? null : LocalTime.parse(str);
//        }
        return null;
    }

}
