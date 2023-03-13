package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NClobTypeHandler implements TypeHandler<String> {

    private String toString(Clob clob) throws SQLException {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }

    @Override
    public void setObject(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        StringReader reader = new StringReader(parameter);
        ps.setCharacterStream(i, reader, parameter.length());
    }

    @Override
    public String getObject(ResultSet rs, int index) throws SQLException {
        Clob clob = rs.getClob(index);
        return toString(clob);
    }

}
