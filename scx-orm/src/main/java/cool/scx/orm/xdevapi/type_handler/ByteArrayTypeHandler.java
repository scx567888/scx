package cool.scx.orm.xdevapi.type_handler;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ByteArrayTypeHandler implements TypeHandler<byte[]> {

    @Override
    public void setObject(PreparedStatement ps, int i, byte[] parameter) throws SQLException {
        ps.setBytes(i, parameter);
    }

    @Override
    public byte[] getObject(Row rs, int index) throws SQLException {
//        return rs.getBytes(index);
        return null;
    }

}
