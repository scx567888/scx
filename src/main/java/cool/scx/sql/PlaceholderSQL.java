package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 1.5.0
 */
final class PlaceholderSQL extends AbstractPlaceholderSQL<Object[]> {

    /**
     * 构建一个占位符 sql
     *
     * @param params a {@link java.lang.String} object
     */
    public PlaceholderSQL(String normalSQL, Object... params) {
        super(false);
        this.normalSQL = normalSQL;
        this.params = params;
    }

    public PlaceholderSQL(String normalSQL, List<Object[]> batchParams) {
        super(true);
        this.normalSQL = normalSQL;
        this.batchParams = batchParams != null ? batchParams : new ArrayList<>();
    }

    @Override
    public PreparedStatement getPreparedStatement0(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //单条数据
        if (params != null) {
            fillPreparedStatement(preparedStatement, params);
        }
        logSQL(preparedStatement);
        return preparedStatement;
    }

    /**
     * a
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatement1(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //根据数据量, 判断是否使用 批量插入
        for (var paramMap : batchParams) {
            if (paramMap != null) {
                fillPreparedStatement(preparedStatement, paramMap);
                preparedStatement.addBatch();
            }
        }
        logBatchSQL(preparedStatement);
        return preparedStatement;
    }

}
