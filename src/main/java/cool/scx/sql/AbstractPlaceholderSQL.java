package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * 抽象的 占位符 SQL
 *
 * @author scx567888
 * @version 1.11.8
 */
public abstract class AbstractPlaceholderSQL<T> {

    /**
     * 是否为批量数据
     */
    final boolean isBatch;

    /**
     * a
     */
    final String normalSQL;

    /**
     * a
     */
    final T params;

    /**
     * a
     */
    final List<T> batchParams;

    /**
     * a
     *
     * @param isBatch     a
     * @param normalSQL   a
     * @param params      a
     * @param batchParams a
     */
    protected AbstractPlaceholderSQL(boolean isBatch, String normalSQL, T params, List<T> batchParams) {
        this.isBatch = isBatch;
        this.normalSQL = normalSQL;
        this.params = params;
        this.batchParams = batchParams;
    }

    /**
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws java.sql.SQLException a
     */
    protected static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
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

    /**
     * <p>normalSQL.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String normalSQL() {
        return normalSQL;
    }

    /**
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    protected abstract PreparedStatement getPreparedStatementFromBatch(Connection con) throws SQLException;

    /**
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    protected abstract PreparedStatement getPreparedStatementFromSingle(Connection con) throws SQLException;

    /**
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    public final PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        return SQLHelper.logSQL(isBatch ? getPreparedStatementFromBatch(con) : getPreparedStatementFromSingle(con));
    }

}
