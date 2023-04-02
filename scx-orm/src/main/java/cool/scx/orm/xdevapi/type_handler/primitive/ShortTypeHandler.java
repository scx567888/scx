package cool.scx.orm.xdevapi.type_handler.primitive;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShortTypeHandler extends _PrimitiveTypeHandler<Short> {

    public ShortTypeHandler(boolean isPrimitive) {
        super(isPrimitive, (short) 0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Short parameter) throws SQLException {
        ps.setShort(i, parameter);
    }

    @Override
    public Short getObject0(Row rs, int index) throws SQLException {
//        return rs.getShort(index);
        return null;
    }

}
