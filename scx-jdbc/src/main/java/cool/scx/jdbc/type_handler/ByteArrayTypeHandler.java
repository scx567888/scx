package cool.scx.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// ByteObjectArrayTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayTypeHandler implements TypeHandler<byte[]> {

    @Override
    public void setObject(PreparedStatement ps, int i, byte[] parameter) throws SQLException {
        ps.setBytes(i, parameter);
    }

    @Override
    public byte[] getObject(ResultSet rs, int index) throws SQLException {
        return rs.getBytes(index);
    }

}
