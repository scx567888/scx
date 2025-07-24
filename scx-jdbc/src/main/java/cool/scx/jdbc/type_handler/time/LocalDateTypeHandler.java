package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDate;

import static cool.scx.common.constant.DateTimeFormatters.yyyy_MM_dd;

/// 为不支持 LocalDate 的数据库添加 string 类型的兼容支持
///
/// @author scx567888
/// @version 0.0.1
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    @Override
    public void setObject(PreparedStatement ps, int i, LocalDate parameter) throws SQLException {
        try {
            ps.setObject(i, parameter);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setString(i, yyyy_MM_dd.format(parameter));
        }
    }

    @Override
    public LocalDate getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, LocalDate.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : yyyy_MM_dd.parse(str, LocalDate::from);
        }
    }

}
