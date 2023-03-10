package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;
import cool.scx.util.ArrayUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ByteObjectArrayTypeHandler implements TypeHandler<Byte[]> {


    @Override
    public void setObject(PreparedStatement ps, int i, Byte[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, ArrayUtils.convertToPrimitiveArray(parameter));
    }

    @Override
    public Byte[] getObject(ResultSet rs, int index) throws SQLException {
        byte[] bytes = rs.getBytes(index);
        if (bytes == null) {
            return null;
        }
        return ArrayUtils.convertToObjectArray(bytes);
    }

}
