package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// LongTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class LongTypeHandler extends PrimitiveTypeHandler<Long> {

    public LongTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0L);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Long parameter) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

}
