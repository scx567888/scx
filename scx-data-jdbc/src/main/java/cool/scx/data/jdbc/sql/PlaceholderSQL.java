package cool.scx.data.jdbc.sql;

import cool.scx.data.jdbc.dialect.Dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * 标准 问号形式 (?) 的占位 SQL
 *
 * @author scx567888
 * @version 0.0.1
 */
final class PlaceholderSQL implements SQL {

    /**
     * 是否为批量数据
     */
    private final boolean isBatch;

    /**
     * a
     */
    private final String normalSQL;

    /**
     * a
     */
    private final Object[] params;

    /**
     * a
     */
    private final List<Object[]> batchParams;

    public PlaceholderSQL(String normalSQL, Object[] params) {
        this.isBatch = false;
        this.normalSQL = normalSQL;
        this.params = params;
        this.batchParams = null;
    }

    /**
     * a
     *
     * @param normalSQL   a
     * @param batchParams a
     */
    public PlaceholderSQL(String normalSQL, List<Object[]> batchParams) {
        this.isBatch = true;
        this.normalSQL = normalSQL;
        this.params = null;
        this.batchParams = batchParams;
    }

    /**
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws SQLException a
     */
    public static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params, Dialect typeHandlerSelector) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue == null) {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            } else {
                var typeHandler = typeHandlerSelector.findTypeHandler(tempValue.getClass());
                typeHandler.setObject(preparedStatement, index, tempValue);
            }
            index = index + 1;
        }
    }

    /**
     * a
     *
     * @return a
     * @throws SQLException a
     */
    private PreparedStatement fillSingle(PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        //单条数据
        if (params != null) {
            fillPreparedStatement(preparedStatement, params, typeHandlerSelector);
        }
        return preparedStatement;
    }

    /**
     * a
     *
     * @return c
     * @throws SQLException c
     */
    private PreparedStatement fillBatch(PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        if (batchParams != null) {
            //根据数据量, 判断是否使用 批量插入
            for (var paramArray : batchParams) {
                if (paramArray != null) {
                    fillPreparedStatement(preparedStatement, paramArray, typeHandlerSelector);
                    preparedStatement.addBatch();
                }
            }
        }
        return preparedStatement;
    }

    @Override
    public Object[] params() {
        return this.params;
    }

    @Override
    public PreparedStatement fillParams(PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        return isBatch ? fillBatch(preparedStatement, typeHandlerSelector) : fillSingle(preparedStatement, typeHandlerSelector);
    }

    @Override
    public String sql() {
        return normalSQL;
    }

}
