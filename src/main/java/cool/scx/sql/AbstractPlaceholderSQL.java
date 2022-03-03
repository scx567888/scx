package cool.scx.sql;

import com.mysql.cj.jdbc.ClientPreparedStatement;

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
abstract class AbstractPlaceholderSQL<T> {

    /**
     * 是否为批量数据
     */
    final boolean isBatch;

    /**
     * a
     */
    String normalSQL;

    /**
     * a
     */
    T params;

    /**
     * a
     */
    List<T> batchParams;

    protected AbstractPlaceholderSQL(boolean isBatch) {
        this.isBatch = isBatch;
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
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws java.sql.SQLException a
     */
    final void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue != null) {
                var tempValueClass = tempValue.getClass();
                //判断是否为数据库(MySQL)直接支持的数据类型
                var mysqlType = SQLTypeHelper.getMySQLType(tempValueClass);
                if (mysqlType != null) {
                    preparedStatement.setObject(index, tempValue, mysqlType);
                } else if (tempValueClass.isEnum()) {//不是则转换做一下特殊处理 枚举我们直接存名称
                    preparedStatement.setString(index, SQLTypeHelper.convertToStringOrNull(tempValue));
                } else {//否则存 json
                    preparedStatement.setString(index, SQLTypeHelper.convertToJsonOrNull(tempValue));
                }
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

    void logBatchSQL(PreparedStatement preparedStatement) throws SQLException {
        if (SQLRunner.logger.isDebugEnabled()) {
            var size = batchParams.size();
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            if (size > 1) {
                SQLRunner.logger.debug(realSQL + "... 额外的 " + (size - 1) + " 项");
            } else {
                SQLRunner.logger.debug(realSQL);
            }
        }
    }

    void logSQL(PreparedStatement preparedStatement) throws SQLException {
        if (SQLRunner.logger.isDebugEnabled()) {
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            SQLRunner.logger.debug(realSQL);
        }
    }

    public abstract PreparedStatement getPreparedStatement1(Connection con) throws SQLException;

    public abstract PreparedStatement getPreparedStatement0(Connection con) throws SQLException;

    public final PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        return isBatch ? getPreparedStatement1(con) : getPreparedStatement0(con);
    }

}
