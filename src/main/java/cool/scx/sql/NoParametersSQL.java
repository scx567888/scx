package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 无参数的 sql
 */
public final class NoParametersSQL extends AbstractPlaceholderSQL<Object> {

    private NoParametersSQL(String noParametersSQL) {
        super(false, noParametersSQL, null, null);
    }

    public static NoParametersSQL of(String noParametersSQL) {
        return new NoParametersSQL(noParametersSQL);
    }

    /**
     * 根据 单条参数获取
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatementFromSingle(Connection con) throws SQLException {
        return con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
    }

    /**
     * 根据批量参数获取
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatementFromBatch(Connection con) throws SQLException {
        return con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
    }

}
