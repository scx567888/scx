package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.aggregation.ExpressionGroupBy;
import cool.scx.data.aggregation.FieldGroupBy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCGroupByParser;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import java.util.ArrayList;

import static cool.scx.common.util.ArrayUtils.tryConcat;
import static cool.scx.common.util.StringUtils.notEmpty;

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
        //1, 创建聚合列
        var selectColumns = createSelectColumn(aggregation);
        //2, 创建 where 子句
        var whereClause = whereParser.parse(beforeAggregateQuery.getWhere());
        //3, 创建 分组 子句
        var groupByColumn = createGroupByColumn(aggregation);
        //4, 创建 Having 子句
        var havingClause = whereParser.parse(afterAggregateQuery.getWhere());
        //5, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(afterAggregateQuery.getOrderBys());
        //6, 创建 SQL
        var sql = GetAggregateSQL(selectColumns, whereClause.expression(), groupByColumn, havingClause.expression(), orderByClauses, afterAggregateQuery.getOffset(), afterAggregateQuery.getLimit());
        var finalParams = tryConcat(whereClause.params(), havingClause.params());
        return SQL.sql(sql, finalParams);
    }

    public SQL buildAggregateFirstSQL(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        //1, 创建聚合列
        var selectColumns = createSelectColumn(aggregation);
        //2, 创建 where 子句
        var whereClause = whereParser.parse(beforeAggregateQuery.getWhere());
        //3, 创建 分组 子句
        var groupByColumn = createGroupByColumn(aggregation);
        //4, 创建 Having 子句
        var havingClause = whereParser.parse(afterAggregateQuery.getWhere());
        //5, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(afterAggregateQuery.getOrderBys());
        //6, 创建 SQL
        var sql = GetAggregateSQL(selectColumns, whereClause.expression(), groupByColumn, havingClause.expression(), orderByClauses, null, 1L);
        var finalParams = tryConcat(whereClause.params(), havingClause.params());
        return SQL.sql(sql, finalParams);
    }

    private String GetAggregateSQL(String[] selectColumns, String whereClause, String[] groupByColumns, String havingClause, String[] orderByClauses, Long offset, Long limit) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        var sql = "SELECT " + String.join(", ", selectColumns) + " FROM " + getTableName() +
                getWhereClause(whereClause) + getGroupByColumns(groupByColumns) +
                getHavingClause(havingClause) + getOrderByClause(orderByClauses);
        return dialect.applyLimit(sql, offset, limit);
    }

    private String getOrderByClause(String[] orderByClauses) {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

    private String getHavingClause(String havingClause) {
        return notEmpty(havingClause) ? " HAVING " + havingClause : "";
    }

    private String getGroupByColumns(String[] groupByColumns) {
        return groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + String.join(", ", groupByColumns) : "";
    }

    private String getWhereClause(String whereClause) {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getTableName() {
        return dialect.quoteIdentifier(table.name());
    }

    public String[] createSelectColumn(Aggregation aggregation) {
        // 需要 聚合的列
        var aggs = aggregation.getAggs();
        // 需要 分组的列 (我们需要在查询的时候 同时查出来 分组的列, 因为如果不查返回的结果实际上没有意义)
        var groupBys = aggregation.getGroupBys();
        var selectColumn = new ArrayList<String>();
        //先添加聚合列
        for (var agg : aggs) {
            //这里别忘了 使用方言包一下引用
            selectColumn.add(agg.expression() + " AS " + dialect.quoteIdentifier(agg.alias()));
        }
        //添加分组列
        for (var groupBy : groupBys) {
            if (groupBy instanceof FieldGroupBy f) {
                var s = groupByParser.parseGroupBy(f);
                selectColumn.add(s);
            }
            if (groupBy instanceof ExpressionGroupBy f) {
                selectColumn.add(f.expression() + " AS " + dialect.quoteIdentifier(f.alias()));
            }
        }
        return selectColumn.toArray(String[]::new);
    }

    public String[] createGroupByColumn(Aggregation aggregation) {
        var groupBys = aggregation.getGroupBys();
        var groupByColumn = new ArrayList<String>();
        for (var groupBy : groupBys) {
            if (groupBy instanceof FieldGroupBy f) {
                var s = groupByParser.parseGroupBy(f);
                groupByColumn.add(s);
            }
            if (groupBy instanceof ExpressionGroupBy f) {
                groupByColumn.add(dialect.quoteIdentifier(f.alias()));
            }
        }
        return groupByColumn.toArray(String[]::new);
    }

}
