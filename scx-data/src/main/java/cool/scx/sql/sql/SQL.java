package cool.scx.sql.sql;

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
     * 数组类型的参数值
     *
     * @return a
     */
    default Object[] params() {
        return new Object[0];
    }

    /**
     * 填充参数
     *
     * @param preparedStatement p
     * @return PreparedStatement 方便链式调用
     * @throws SQLException a
     */
    default PreparedStatement fillParams(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

}
