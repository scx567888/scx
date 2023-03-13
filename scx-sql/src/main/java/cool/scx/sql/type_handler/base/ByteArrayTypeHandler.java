package cool.scx.sql.type_handler.base;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
