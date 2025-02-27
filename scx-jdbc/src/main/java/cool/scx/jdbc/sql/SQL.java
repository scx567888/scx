package cool.scx.jdbc.sql;

import java.util.List;
import java.util.Map;

/// 可包含参数的 SQL
///
/// @author scx567888
/// @version 0.0.1
public interface SQL {

    static SQL sql(String sql, Object... params) {
        return new DefaultSQL(sql, params);
    }

    static SQL sql(String sql, List<Object[]> batchParams) {
        return new DefaultSQL(sql, batchParams);
    }

    static SQL sqlNamed(String namedSQL, Map<String, Object> params) {
        return new NamedSQL(namedSQL, params);
    }

    static SQL sqlNamed(String namedSQL, List<Map<String, Object>> batchParams) {
        return new NamedSQL(namedSQL, batchParams);
    }

    /// SQL 语句
    ///
    /// @return sql
    String sql();

    /// 参数值
    ///
    /// @return params
    Object[] params();

    /// 批量插入参数值
    ///
    /// @return batchParams
    List<Object[]> batchParams();

    /// 是否为批量参数
    ///
    /// @return isBatch
    boolean isBatch();

}
