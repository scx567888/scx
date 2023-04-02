package cool.scx.orm.xdevapi.type_handler;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static cool.scx.util.ArrayUtils.toPrimitive;

public class ByteObjectArrayTypeHandler implements TypeHandler<Byte[]> {

    @Override
    public void setObject(PreparedStatement ps, int i, Byte[] parameter) throws SQLException {
        ps.setBytes(i, toPrimitive(parameter));
    }

    @Override
    public Byte[] getObject(Row rs, int index) throws SQLException {
//        byte[] bytes = rs.getBytes(index);
//        return bytes == null ? null : toWrapper(bytes);
        return null;
    }

}
