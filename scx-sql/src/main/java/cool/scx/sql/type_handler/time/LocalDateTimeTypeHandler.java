package cool.scx.sql.type_handler.time;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDateTime;

/**
 * 为不支持 LocalDateTime 的数据库添加 string 类型的兼容支持
 */
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, LocalDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : LocalDateTime.parse(str);
        }
    }

}
