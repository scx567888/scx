package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobTypeHandler implements TypeHandler<byte[]> {


    @Override
    public void setObject(PreparedStatement ps, int i, byte[] parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(parameter);
        ps.setBinaryStream(i, bis, parameter.length);
    }

    @Override
    public byte[] getObject(ResultSet rs, int index) throws SQLException {
        Blob blob = rs.getBlob(index);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        return returnValue;
    }

}
