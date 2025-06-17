package cool.scx.jdbc.sql;

import java.util.Collection;
import java.util.List;

/// 代表一种特殊的 SQL 参数类型, 用于处理带有列表形式的命名参数.
///
/// 通常用于 SQL 查询中, 如 IN 子句 例如: `SELECT * FROM table WHERE field IN (:values)``
/// 该类允许用户将多个值传递给 SQL 查询, 而不是仅仅传递一个单一值.
///
/// 注意: 此参数类型只能用于带有命名参数的 SQL 查询, 且仅适用于 NamedSQL.
///
/// 示例:
/// ```java
/// var inList = new NamedSQLListParameter(1, 2, 3, 4);
///```
/// 对应 SQL 查询: `SELECT * FROM table WHERE field IN (:inList)`
///
/// @author scx567888
/// @version 0.0.1
/// @see NamedSQL
public record NamedSQLListParameter(Collection<?> values) {

    public static NamedSQLListParameter of(Object... values) {
        return new NamedSQLListParameter(List.of(values));
    }

    public static NamedSQLListParameter of(Collection<?> values) {
        return new NamedSQLListParameter(values);
    }

}
