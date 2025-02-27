package cool.scx.jdbc.type_handler.math;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// BigDecimalTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class BigDecimalTypeHandler implements TypeHandler<BigDecimal> {

    @Override
    public void setObject(PreparedStatement ps, int i, BigDecimal parameter) throws SQLException {
        ps.setBigDecimal(i, parameter);
    }

    @Override
    public BigDecimal getObject(ResultSet rs, int index) throws SQLException {
        return rs.getBigDecimal(index);
    }

}
