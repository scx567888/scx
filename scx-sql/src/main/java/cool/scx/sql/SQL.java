package cool.scx.sql;

import cool.scx.sql.sql.NamedParameterSQL;
import cool.scx.sql.sql.NormalSQL;
import cool.scx.sql.sql.PlaceholderSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 可包含参数的 SQL
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface SQL {

    static SQL ofNormal(String normalSQL) {
        return new NormalSQL(normalSQL);
    }

    static SQL ofPlaceholder(String normalSQL, Object... params) {
        return new PlaceholderSQL(normalSQL, params);
    }

    static SQL ofPlaceholder(String normalSQL, List<Object[]> batchParams) {
        return new PlaceholderSQL(normalSQL, batchParams);
    }

    static SQL ofNamedParameter(String namedParameterSQL, Map<String, Object> params) {
        return new NamedParameterSQL(namedParameterSQL, params);
    }

    static SQL ofNamedParameter(String namedParameterSQL, List<Map<String, Object>> batchParams) {
        return new NamedParameterSQL(namedParameterSQL, batchParams);
    }

    /**
     * 真正的 SQL 语句
     *
     * @return a {@link java.lang.String} object
     */
    String sql();

    /**
     * a
     *
     * @param con a
     * @return a
     * @throws java.sql.SQLException a
     */
    PreparedStatement getPreparedStatement(Connection con) throws SQLException;

    /**
     * 数组类型的参数值
     *
     * @return a
     */
    Object[] params();

}
