package cool.scx.orm.xdevapi.type_handler.math;

import com.mysql.cj.xdevapi.Row;
import cool.scx.orm.xdevapi.type_handler.TypeHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BigDecimalTypeHandler implements TypeHandler<BigDecimal> {

    @Override
    public void setObject(PreparedStatement ps, int i, BigDecimal parameter) throws SQLException {
        ps.setBigDecimal(i, parameter);
    }

    @Override
    public BigDecimal getObject(Row rs, int index) throws SQLException {
        return rs.getBigDecimal(index);
    }

}
