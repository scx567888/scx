package cool.scx.data.jdbc.type_handler.time;

import cool.scx.data.jdbc.type_handler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 为不支持 LocalDateTime 的数据库添加 string 类型的兼容支持
 */
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public void setObject(PreparedStatement ps, int i, LocalDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, LocalDateTime.class);
    }

}
