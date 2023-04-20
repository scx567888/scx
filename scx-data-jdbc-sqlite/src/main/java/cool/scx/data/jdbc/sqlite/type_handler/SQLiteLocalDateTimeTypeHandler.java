package cool.scx.data.jdbc.sqlite.type_handler;

import cool.scx.data.jdbc.sqlite.SQLiteDialect;
import cool.scx.data.jdbc.type_handler.time.LocalDateTimeTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SQLiteLocalDateTimeTypeHandler extends LocalDateTimeTypeHandler {

    public static final Logger logger = LoggerFactory.getLogger(SQLiteDialect.class);

    @Override
    public void setObject(PreparedStatement ps, int i, LocalDateTime parameter) throws SQLException {
        logger.warn("当前驱动不支持 LocalDateTime, 尝试使用 Timestamp 进行转换 !!!");
        ps.setTimestamp(i, Timestamp.valueOf(parameter));
    }

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        logger.warn("当前驱动不支持 LocalDateTime, 尝试使用 Timestamp 进行转换 !!!");
        var timestamp = rs.getTimestamp(index);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

}
