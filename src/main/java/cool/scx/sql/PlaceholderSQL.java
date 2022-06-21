package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 标准 问号形式 (?) 的占位 SQL
 *
 * @author scx567888
 * @version 1.5.0
 */
public final class PlaceholderSQL extends AbstractPlaceholderSQL<Object[]> {

    private PlaceholderSQL(boolean isBatch, String normalSQL, Object[] params, List<Object[]> batchParams) {
        super(isBatch, normalSQL, params, batchParams);
    }

    /**
     * a
     *
     * @param normalSQL a
     * @param params    a
     * @return a
     */
    public static PlaceholderSQL of(String normalSQL, Object... params) {
        return new PlaceholderSQL(false, normalSQL, params, null);
    }

    /**
     * a
     *
     * @param normalSQL   a
     * @param batchParams a
     * @return a
     */
    public static PlaceholderSQL ofBatch(String normalSQL, List<Object[]> batchParams) {
        if (batchParams == null) {
            batchParams = new ArrayList<>();
        }
        return new PlaceholderSQL(true, normalSQL, null, batchParams);
    }

    @Override
    public PreparedStatement getPreparedStatementFromSingle(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //单条数据
        if (params != null) {
            fillPreparedStatement(preparedStatement, params);
        }
        return preparedStatement;
    }

    @Override
    public Object[] objectArrayParams() {
        return this.params;
    }

    /**
     * a
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatementFromBatch(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //根据数据量, 判断是否使用 批量插入
        for (var paramArray : batchParams) {
            if (paramArray != null) {
                fillPreparedStatement(preparedStatement, paramArray);
                preparedStatement.addBatch();
            }
        }
        return preparedStatement;
    }

}
