package cool.scx.orm.xdevapi.type_handler.primitive;

import com.mysql.cj.xdevapi.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FloatTypeHandler extends _PrimitiveTypeHandler<Float> {

    public FloatTypeHandler(boolean isPrimitive) {
        super(isPrimitive, 0F);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getObject0(Row rs, int index) throws SQLException {
//        return rs.getFloat(index);
        return null;
    }

}
