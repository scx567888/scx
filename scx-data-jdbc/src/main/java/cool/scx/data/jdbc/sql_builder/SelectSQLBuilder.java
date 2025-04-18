package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCGroupByParser;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.common.util.RandomUtils.randomString;
import static cool.scx.data.jdbc.sql_builder.Helper.filterByFieldPolicy;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Select;

public class SelectSQLBuilder {

    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCWhereParser whereParser;
    private final JDBCGroupByParser groupByParser;
    private final JDBCOrderByParser orderByParser;

    public SelectSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCWhereParser whereParser, JDBCGroupByParser groupByParser, JDBCOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.groupByParser = groupByParser;
        this.orderByParser = orderByParser;
    }

    /// 创建虚拟查询列
    public static String[] createVirtualSelectColumns(FieldPolicy fieldFilter, Dialect dialect) {
        var fieldExpressions = fieldFilter.fieldExpressions();
        var virtualSelectColumns = new String[fieldExpressions.size()];
        int i = 0;
        for (var fieldExpression : fieldExpressions.entrySet()) {
            var fieldName = fieldExpression.getKey();
            var expression = fieldExpression.getValue();
            // 这个虚拟列 因为可能在表中不存在 所以此处不进行名称映射了 直接引用包装一下即可  
            virtualSelectColumns[i] = expression + " AS " + dialect.quoteIdentifier(fieldName);
            i = i + 1;
        }
        return virtualSelectColumns;
    }

    public SQL buildSelectSQL(Query query, FieldPolicy fieldPolicy) {
        //1, 过滤查询列
        var selectColumns = filterByFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = tryConcatAny(selectColumns, (Object[]) virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //5, 创建 groupBy 子句
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        //7, 创建 SQL
        var sql = Select(finalSelectColumns)
                .From(table)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(query.getOffset(), query.getLimit())
                .GetSQL(dialect);
        return sql(sql, whereClause.params());
    }

    public SQL buildGetSQL(Query query, FieldPolicy fieldPolicy) {
        //1, 过滤查询列
        var selectColumns = filterByFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = tryConcatAny(selectColumns, (Object[]) virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //5, 创建 groupBy 子句
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        //7, 创建 SQL
        var sql = Select(finalSelectColumns)
                .From(table)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(null, 1L)
                .GetSQL(dialect);
        return sql(sql, whereClause.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    public SQL buildGetSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        var sql0 = buildGetSQL(query, fieldPolicy);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(dialect);
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    public SQL buildSelectSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        var sql0 = buildSelectSQL(query, fieldPolicy);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(dialect);
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

}
