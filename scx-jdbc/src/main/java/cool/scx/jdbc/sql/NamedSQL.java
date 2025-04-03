package cool.scx.jdbc.sql;

import cool.scx.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/// 具名参数 SQL
///
/// @author scx567888
/// @version 0.0.1
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
