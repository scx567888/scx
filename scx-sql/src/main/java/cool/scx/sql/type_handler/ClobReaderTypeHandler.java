package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobReaderTypeHandler implements TypeHandler<Reader> {

    @Override
    public void setObject(PreparedStatement ps, int i, Reader parameter, JdbcType jdbcType) throws SQLException {
        ps.setClob(i, parameter);
    }

    @Override
    public Reader getObject(ResultSet rs, int index) throws SQLException {
        var clob = rs.getClob(index);
        if (clob == null) {
            return null;
        }
        return clob.getCharacterStream();
    }

}
