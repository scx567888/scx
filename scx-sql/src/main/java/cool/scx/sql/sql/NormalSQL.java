package cool.scx.sql.sql;

import cool.scx.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 无参数的 cool.scx.sql
 *
 * @author scx567888
 * @version 0.0.7
 */
public final class NormalSQL implements SQL {

    /**
     * a
     */
    private final String normalSQL;

    public NormalSQL(String normalSQL) {
        this.normalSQL = normalSQL;
    }

    @Override
    public String sql() {
        return normalSQL;
    }

    @Override
    public PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    public Object[] params() {
        return new Object[0];
    }

}
