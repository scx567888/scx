package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalTime;

import static cool.scx.common.constant.DateTimeFormatters.HH_mm_ss;

/// 为不支持 LocalTime 的数据库添加 string 类型的兼容支持
///
/// @author scx567888
/// @version 0.0.1
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, LocalTime parameter) throws SQLException {
        try {
            ps.setObject(i, parameter);
        } catch (SQLFeatureNotSupportedException e) {
            ps.setString(i, HH_mm_ss.format(parameter));
        }
    }

    @Override
    public LocalTime getObject(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return rs.getObject(columnIndex, LocalTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            var str = rs.getString(columnIndex);
            return str == null ? null : HH_mm_ss.parse(str, LocalTime::from);
        }
    }

}
