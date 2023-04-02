package cool.scx.orm.jdbc.type_handler.time;

import cool.scx.orm.jdbc.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalTime;

/**
 * 为不支持 LocalTime 的数据库添加 string 类型的兼容支持
 */
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public LocalTime getObject(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return rs.getObject(columnIndex, LocalTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(columnIndex);
            return str == null ? null : LocalTime.parse(str);
        }
    }

}
