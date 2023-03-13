package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler implements TypeHandler<Byte> {

    @Override
    public void setObject(PreparedStatement ps, int i, Byte parameter) throws SQLException {
        ps.setByte(i, parameter);
    }

    @Override
    public Byte getObject(ResultSet rs, int index) throws SQLException {
        byte result = rs.getByte(index);
        return rs.wasNull() ? null : result;
    }

}
