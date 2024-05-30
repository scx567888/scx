package cool.scx.jdbc.type_handler;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//todo 在驱动不支持时 转换为 string 或者 byte[] 存储 ?
public class BlobInputStreamTypeHandler implements TypeHandler<InputStream> {

    @Override
    public void setObject(PreparedStatement ps, int i, InputStream parameter) throws SQLException {
        ps.setBlob(i, parameter);
    }

    @Override
    public InputStream getObject(ResultSet rs, int index) throws SQLException {
        var blob = rs.getBlob(index);
        return blob == null ? null : blob.getBinaryStream();
    }

}
