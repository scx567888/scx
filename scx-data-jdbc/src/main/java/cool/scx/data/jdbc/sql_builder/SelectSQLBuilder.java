package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.common.util.RandomUtils.randomString;
import static cool.scx.data.jdbc.A.filterByFieldPolicy;
import static cool.scx.data.jdbc.DataJDBCHelper.createVirtualSelectColumns;
import static cool.scx.data.jdbc.DataJDBCHelper.filter;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Select;

public class SelectSQLBuilder {

    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCDaoWhereParser whereParser;
    private final JDBCDaoGroupByParser groupByParser;
    private final JDBCDaoOrderByParser orderByParser;

    public SelectSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCDaoWhereParser whereParser, JDBCDaoGroupByParser groupByParser, JDBCDaoOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.groupByParser = groupByParser;
        this.orderByParser = orderByParser;
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
        var selectColumns = filter(fieldPolicy, table);
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
