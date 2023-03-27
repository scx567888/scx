package cool.scx.sql.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler extends _PrimitiveTypeHandler<Long> {

    public LongTypeHandler(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Long parameter) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getPrimitiveObject(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

}
