package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;
import cool.scx.util.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobByteObjectArrayTypeHandler implements TypeHandler<Byte[]> {

    @Override
    public void setObject(PreparedStatement ps, int i, Byte[] parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(ArrayUtils.toPrimitive(parameter));
        ps.setBinaryStream(i, bis, parameter.length);
    }

    @Override
    public Byte[] getObject(ResultSet rs, int index) throws SQLException {
        Blob blob = rs.getBlob(index);
        if (blob == null) {
            return null;
        }
        return ArrayUtils.toWrapper(blob.getBytes(1, (int) blob.length()));
    }

}
