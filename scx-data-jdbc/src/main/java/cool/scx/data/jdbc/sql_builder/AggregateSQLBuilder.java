package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.aggregation.Agg;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.aggregation.GroupBy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCGroupByParser;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.filterByQueryFieldPolicy;
import static cool.scx.jdbc.sql.SQL.sql;

public class AggregateSQLBuilder {


    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCWhereParser whereParser;
    private final JDBCGroupByParser groupByParser;
    private final JDBCOrderByParser orderByParser;

    public AggregateSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCWhereParser whereParser, JDBCGroupByParser groupByParser, JDBCOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.groupByParser = groupByParser;
        this.orderByParser = orderByParser;
    }

    public SQL buildAggregateSQL(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        // 需要 聚合的列
        Agg[] aggs = aggregation.getAggs();
        // 需要 分组的列
        GroupBy[] groupBys = aggregation.getGroupBys();
        //1, 过滤查询列
        var selectColumns = filterByQueryFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = tryConcatAny(selectColumns, (Object[]) virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        //7, 创建 SQL
        var sql = GetAggregateSQL(finalSelectColumns, whereClause.whereClause(), orderByClauses, query.getOffset(), query.getLimit());
        return sql(sql, whereClause.params());
    }

    public SQL buildAggregateFirstSQL(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        //1, 过滤查询列
        var selectColumns = filterByQueryFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = tryConcatAny(selectColumns, (Object[]) virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        //7, 创建 SQL
        var sql = GetAggregateSQL(finalSelectColumns, whereClause.whereClause(), orderByClauses, null, 1L);
        return sql(sql, whereClause.params());
    }

    private String GetAggregateSQL(Object[] selectColumns, String whereClause, String[] orderByClauses, Long offset, Long limit) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        var sql = "SELECT " + getSelectColumns(selectColumns) + " FROM " + getTableName() + getWhereClause(whereClause) + getOrderByClause(orderByClauses);
        return dialect.getLimitSQL(sql, offset, limit);
    }

}
