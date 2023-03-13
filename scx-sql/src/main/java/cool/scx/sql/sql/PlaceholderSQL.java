package cool.scx.sql.sql;

import cool.scx.sql.SQL;
import cool.scx.sql.SQLHelper;

import java.sql.*;
import java.util.List;

/**
 * 标准 问号形式 (?) 的占位 SQL
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class PlaceholderSQL implements SQL {

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
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    private PreparedStatement getPreparedStatementFromSingle(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //单条数据
        if (params != null) {
            fillPreparedStatement(preparedStatement, params);
        }
        return preparedStatement;
    }

    @Override
    public Object[] params() {
        return this.params;
    }

    /**
     * a
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    private PreparedStatement getPreparedStatementFromBatch(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        if (batchParams != null) {
            //根据数据量, 判断是否使用 批量插入
            for (var paramArray : batchParams) {
                if (paramArray != null) {
                    fillPreparedStatement(preparedStatement, paramArray);
                    preparedStatement.addBatch();
                }
            }
        }
        return preparedStatement;
    }

    @Override
    public String sql() {
        return normalSQL;
    }

    @Override
    public PreparedStatement prepareStatement(Connection con) throws SQLException {
        return isBatch ? getPreparedStatementFromBatch(con) : getPreparedStatementFromSingle(con);
    }

    /**
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws SQLException a
     */
    public static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue != null) {
                var tempValueClass = tempValue.getClass();
                //判断是否为数据库(MySQL)直接支持的数据类型
                var mysqlType = SQLHelper.getMySQLType(tempValueClass);
                if (mysqlType != null) {
                    preparedStatement.setObject(index, tempValue, mysqlType);
                } else if (tempValueClass.isEnum()) {//不是则转换做一下特殊处理 枚举我们直接存名称
                    preparedStatement.setString(index, SQLHelper.convertToStringOrNull(tempValue));
                } else {//否则存 json
                    preparedStatement.setString(index, SQLHelper.convertToJsonOrNull(tempValue));
                }
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

}
