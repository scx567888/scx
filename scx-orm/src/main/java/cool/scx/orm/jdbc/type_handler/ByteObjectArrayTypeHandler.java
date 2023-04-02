package cool.scx.orm.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cool.scx.util.ArrayUtils.toPrimitive;
import static cool.scx.util.ArrayUtils.toWrapper;

public class ByteObjectArrayTypeHandler implements TypeHandler<Byte[]> {

    @Override
    public void setObject(PreparedStatement ps, int i, Byte[] parameter) throws SQLException {
        ps.setBytes(i, toPrimitive(parameter));
    }

    @Override
    public Byte[] getObject(ResultSet rs, int index) throws SQLException {
        byte[] bytes = rs.getBytes(index);
        return bytes == null ? null : toWrapper(bytes);
    }

}
