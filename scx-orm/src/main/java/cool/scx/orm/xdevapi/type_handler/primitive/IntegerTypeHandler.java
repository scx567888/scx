package cool.scx.orm.xdevapi.type_handler.primitive;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerTypeHandler extends _PrimitiveTypeHandler<Integer> {

    public IntegerTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Integer parameter) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getObject0(Row rs, int index) throws SQLException {
        return rs.getInt(index);
    }

}
