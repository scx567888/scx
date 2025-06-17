package cool.scx.jdbc.sql;

import cool.scx.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/// 代表带有具名参数的 SQL 查询. 通过此类, 用户可以方便地构造 SQL 查询, 使用命名参数代替传统的 `?` 占位符.
///
/// 具名参数通过 `:` 后跟参数名（如 `:paramName`）表示. 在执行 SQL 查询时, 具名参数会被其实际值替换,
/// 提供了更具可读性的 SQL 语句.
///
/// 特别地, `NamedSQL` 类支持两种使用方式:
/// 1. **单个 SQL 查询**: 构造一个带有具名参数的 SQL 查询, 支持替换具名参数为相应的值.
/// 2. **批量查询**: 在批量操作（如批量插入、更新）时, 支持将不同的参数集合对应到 SQL 查询中的具名参数.
///
///
/// **特殊情况: 处理 NamedSQLListParameter**
/// 当 SQL 查询中包含类型为 `NamedSQLListParameter` 的具名参数时（例如 `IN` 子句）,
/// 该类会自动展开这个参数, 生成多个 `?` 占位符并将其值作为多个参数传入查询中.
///
///
/// 示例:
/// ```java
/// var = Map.of(
///   "inList", new NamedSQLListParameter(1, 2, 3)
///);
/// var namedSQL = new NamedSQL("SELECT * FROM table WHERE field IN (:inList)", params);
///```
/// 对应 SQL 查询: `SELECT * FROM table WHERE field IN (?, ?, ?)`
///
///
/// @author scx567888
/// @version 0.0.1
/// @see NamedSQLListParameter
final class NamedSQL implements SQL {

    /// 具名参数匹配的正则表达式
    private static final Pattern NAMED_SQL_PATTERN = Pattern.compile(":([\\w.-]+)");

    private final String sql;
    private final Object[] params;
    private final List<Object[]> batchParams;
    private final boolean isBatch;

    NamedSQL(String namedSQL, Map<String, Object> params) {
        // 匹配所有命名参数
        var matcher = NAMED_SQL_PATTERN.matcher(namedSQL);
        var tempSQL = new StringBuilder();
        var tempParams = new ArrayList<>();
        while (matcher.find()) {
            //这里 需要根据 参数类型来做特殊处理
            var g = matcher.group(1);
            var value = params.get(g);
            //如果是 特殊参数 我们特殊处理 进行展开
            if (value instanceof NamedSQLListParameter n) {
                var values = n.values();
                tempParams.addAll(values);
                matcher.appendReplacement(tempSQL, StringUtils.repeat("?", ", ", values.size()));
            } else {// 其余则使用普通 ?
                tempParams.add(value);
                matcher.appendReplacement(tempSQL, "?");
            }
        }
        matcher.appendTail(tempSQL);

        //初始化字段
        this.sql = tempSQL.toString();
        this.params = tempParams.toArray();
        this.batchParams = new ArrayList<>();
        this.isBatch = false;
    }

    NamedSQL(String namedSQL, List<Map<String, Object>> batchParams) {
        // 匹配所有命名参数
        var matcher = NAMED_SQL_PATTERN.matcher(namedSQL);
        var tempSQL = new StringBuilder();
        var tempNameIndex = new ArrayList<String>();
        while (matcher.find()) {
            //批量时 我们不支持特殊参数 所以统一使用 ? 来表示
            var g = matcher.group(1);
            tempNameIndex.add(g);
            matcher.appendReplacement(tempSQL, "?");
        }
        matcher.appendTail(tempSQL);

        // 这里将 map 转换为数组
        var nameIndex = tempNameIndex.toArray(String[]::new);
        var tempBatchParams = new ArrayList<Object[]>();
        for (var p : batchParams) {
            var o = new Object[nameIndex.length];
            for (int i = 0; i < nameIndex.length; i = i + 1) {
                o[i] = p.get(nameIndex[i]);
            }
            tempBatchParams.add(o);
        }

        //初始化字段
        this.sql = tempSQL.toString();
        this.params = new Object[]{};
        this.batchParams = tempBatchParams;
        this.isBatch = true;
    }

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public List<Object[]> batchParams() {
        return batchParams;
    }

    @Override
    public boolean isBatch() {
        return isBatch;
    }

}
