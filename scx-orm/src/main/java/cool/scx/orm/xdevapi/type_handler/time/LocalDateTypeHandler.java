package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * 为不支持 LocalDate 的数据库添加 string 类型的兼容支持
 */
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    @Override
    public LocalDate getObject(Row rs, int index) throws SQLException {
//        try {
//            return rs.getObject(index, LocalDate.class);
//        } catch (SQLFeatureNotSupportedException e) {
//            var str = rs.getString(index);
//            return str == null ? null : LocalDate.parse(str);
//        }
        return null;
    }

}
