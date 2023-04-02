package cool.scx.orm.jdbc.type_handler.math;

import cool.scx.orm.jdbc.type_handler.TypeHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigIntegerTypeHandler implements TypeHandler<BigInteger> {

    @Override
    public void setObject(PreparedStatement ps, int i, BigInteger parameter) throws SQLException {
        ps.setBigDecimal(i, new BigDecimal(parameter));
    }

    @Override
    public BigInteger getObject(ResultSet rs, int index) throws SQLException {
        BigDecimal bigDecimal = rs.getBigDecimal(index);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }

}
