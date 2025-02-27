package cool.scx.jdbc.type_handler;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// ClobReaderTypeHandler
/// todo 在驱动不支持时 转换为 string 或者 byte[] 存储 ?
///
/// @author scx567888
/// @version 0.0.1
public class ClobReaderTypeHandler implements TypeHandler<Reader> {

    @Override
    public void setObject(PreparedStatement ps, int i, Reader parameter) throws SQLException {
        ps.setClob(i, parameter);
    }

    @Override
    public Reader getObject(ResultSet rs, int index) throws SQLException {
        var clob = rs.getClob(index);
        return clob == null ? null : clob.getCharacterStream();
    }

}
