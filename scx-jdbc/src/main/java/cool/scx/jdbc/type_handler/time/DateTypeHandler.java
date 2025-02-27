package cool.scx.jdbc.type_handler.time;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/// 此处因为 jdbc 的设计非常混乱 所以说明一下
///
/// [java.sql.Date] 是指 只有日期没有时间
///
/// [java.sql.Time] 是指 只有时间没有日期
///
/// [java.sql.Timestamp] 是指既有日期又有时间
///
/// 我们此处使用 [java.sql.Timestamp] 进行存储, 同时建议用户使用 [java.time.LocalDateTime] 等替换 [java.util.Date]
///
/// @author scx567888
/// @version 0.0.1
public class DateTypeHandler implements TypeHandler<Date> {

    @Override
    public void setObject(PreparedStatement ps, int i, Date parameter) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    public Date getObject(ResultSet rs, int index) throws SQLException {
        var sqlTimestamp = rs.getTimestamp(index);
        return sqlTimestamp == null ? null : new Date(sqlTimestamp.getTime());
    }

}
