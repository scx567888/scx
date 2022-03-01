package cool.scx.sql;

import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * a
 *
 * @author scx567888
 * @version 1.5.0
 */
final class PlaceholderSQL extends AbstractPlaceholderSQL {

    /**
     * 具名参数名称索引
     */
    private final Object[] params;

    /**
     * 构建一个占位符 sql
     *
     * @param params a {@link java.lang.String} object
     */
    public PlaceholderSQL(String normalSQL, Object... params) {
        this.normalSQL = normalSQL;
        this.params = params;
    }

    @Override
    public PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        fillPreparedStatement(preparedStatement, params);
        if (SQLRunner.logger.isDebugEnabled()) {
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            SQLRunner.logger.debug(realSQL);
        }
        return preparedStatement;
    }

}
