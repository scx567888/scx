package cool.scx.jdbc;

/// JDBCHelper
///
/// @author scx567888
/// @version 0.0.1
public final class JDBCHelper {

    /// 格式化 SQL 和 参数, 使用简单的占位符替换方式 一般用来辅助编写 Dialect
    ///
    /// @param sql    sql
    /// @param params 参数
    /// @return sql
    public static String getSqlWithValues(String sql, Object[] params) {
        final StringBuilder sb = new StringBuilder();

        // iterate over the characters in the query replacing the parameter placeholders
        // with the actual values
        var currentParameter = 0;
        for (var pos = 0; pos < sql.length(); pos = pos + 1) {
            char character = sql.charAt(pos);
            if (character == '?' && currentParameter <= params.length) {
                // replace with parameter value
                var value = params[currentParameter];
                sb.append(value != null ? value.toString() : "NULL");
                currentParameter = currentParameter + 1;
            } else {
                sb.append(character);
            }
        }

        return sb.toString();
    }

}
