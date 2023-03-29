package cool.scx.sql.type_handler.time;

import cool.scx.sql.type_handler.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * 为不支持 LocalDateTime 的数据库添加 string 类型的兼容支持
 */
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeTypeHandler.class);

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getObject(index, LocalDateTime.class);
        } catch (SQLFeatureNotSupportedException e) {
            logger.warn("当前驱动不支持 LocalDateTime, 尝试使用 String 进行转换 !!!", e);
            var str = rs.getString(index);
            if (str != null) {
                try {
                    return LocalDateTime.parse(str);
                } catch (DateTimeParseException de) {
                    logger.warn("当前驱动不支持 LocalDateTime, 尝试使用 String 进行转换失败 !!!", e);
                }
            }
            return null;
        }
    }

}
