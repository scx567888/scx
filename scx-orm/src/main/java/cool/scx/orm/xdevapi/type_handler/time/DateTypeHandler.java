package cool.scx.orm.xdevapi.type_handler.time;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 此处因为 jdbc 的设计非常混乱 所以说明一下
 * <br>
 * {@link java.sql.Date} 是指 只有日期没有时间
 * <br>
 * {@link java.sql.Time} 是指 只有时间没有日期
 * <br>
 * {@link Timestamp} 是指既有日期又有时间
 * <br>
 * 我们此处使用 {@link Timestamp} 进行存储, 同时建议用户使用 {@link  java.time.LocalDateTime} 等替换 {@link  Date}
 */
public class DateTypeHandler implements TypeHandler<Date> {

    @Override
    public void setObject(PreparedStatement ps, int i, Date parameter) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    public Date getObject(Row rs, int index) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(index);
        return sqlTimestamp == null ? null : new Date(sqlTimestamp.getTime());
    }

}
