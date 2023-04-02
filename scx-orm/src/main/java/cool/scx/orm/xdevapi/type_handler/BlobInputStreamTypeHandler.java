package cool.scx.orm.xdevapi.type_handler;

import com.mysql.cj.xdevapi.Row;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//todo 在驱动不支持时 转换为 string 或者 byte[] 存储 ?
public class BlobInputStreamTypeHandler implements TypeHandler<InputStream> {

    @Override
    public void setObject(PreparedStatement ps, int i, InputStream parameter) throws SQLException {
        ps.setBlob(i, parameter);
    }

    @Override
    public InputStream getObject(Row rs, int index) throws SQLException {
//        var blob = rs.getBlob(index);
//        return blob == null ? null : blob.getBinaryStream();
        return null;
    }

}
