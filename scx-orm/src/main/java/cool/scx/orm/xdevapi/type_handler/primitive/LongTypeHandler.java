package cool.scx.orm.xdevapi.type_handler.primitive;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongTypeHandler extends _PrimitiveTypeHandler<Long> {

    public LongTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0L);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Long parameter) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getObject0(Row rs, int index) throws SQLException {
        return rs.getLong(index);
    }

}
