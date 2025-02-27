package cool.scx.jdbc.type_handler.math;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// BigIntegerTypeHandler
///
/// @author scx567888
/// @version 0.0.1
public class BigIntegerTypeHandler implements TypeHandler<BigInteger> {

    @Override
    public void setObject(PreparedStatement ps, int i, BigInteger parameter) throws SQLException {
        ps.setBigDecimal(i, new BigDecimal(parameter));
    }

    @Override
    public BigInteger getObject(ResultSet rs, int index) throws SQLException {
        var bigDecimal = rs.getBigDecimal(index);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }

}
