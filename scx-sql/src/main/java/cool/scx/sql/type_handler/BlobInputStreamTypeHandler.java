package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobInputStreamTypeHandler implements TypeHandler<InputStream> {

    @Override
    public void setObject(PreparedStatement ps, int i, InputStream parameter, JdbcType jdbcType) throws SQLException {
        ps.setBlob(i, parameter);
    }

    @Override
    public InputStream getObject(ResultSet rs, int index) throws SQLException {
        var blob = rs.getBlob(index);
        if (blob == null) {
            return null;
        }
        return blob.getBinaryStream();
    }

}
