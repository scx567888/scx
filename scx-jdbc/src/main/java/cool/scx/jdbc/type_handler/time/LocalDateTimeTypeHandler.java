package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDateTime;

import static cool.scx.common.constant.DateTimeFormatters.yyyy_MM_dd_HH_mm_ss;

/// 为不支持 LocalDateTime 的数据库添加 string 类型的兼容支持
///
/// @author scx567888
/// @version 0.0.1
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, LocalDateTime parameter) throws SQLException {
        try {
            ps.setObject(i, parameter);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setString(i, yyyy_MM_dd_HH_mm_ss.format(parameter));
        }
    }

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, LocalDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(index);
            return str == null ? null : yyyy_MM_dd_HH_mm_ss.parse(str, LocalDateTime::from);
        }
    }

}
