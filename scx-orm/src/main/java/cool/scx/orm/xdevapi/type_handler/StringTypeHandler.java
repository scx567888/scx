package cool.scx.orm.xdevapi.type_handler;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {

    @Override
    public void setObject(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getObject(Row rs, int index) throws SQLException {
        return rs.getString(index);
    }

}
