package cool.scx.jdbc.type_handler.primitive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// ByteTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class ByteTypeHandler extends PrimitiveTypeHandler<Byte> {

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
