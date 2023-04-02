package cool.scx.orm.jdbc.type_handler.time;

import cool.scx.orm.jdbc.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDate;

/**
 * 为不支持 LocalDate 的数据库添加 string 类型的兼容支持
 */
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    @Override
    public LocalDate getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, LocalDate.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : LocalDate.parse(str);
        }
    }

}
