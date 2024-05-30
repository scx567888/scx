package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler extends _PrimitiveTypeHandler<Byte> {

    public ByteTypeHandler(boolean isPrimitive) {
        super(isPrimitive, (byte) 0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Byte parameter) throws SQLException {
        ps.setByte(i, parameter);
    }

    @Override
    public Byte getObject0(ResultSet rs, int index) throws SQLException {
        return rs.getByte(index);
    }

}
