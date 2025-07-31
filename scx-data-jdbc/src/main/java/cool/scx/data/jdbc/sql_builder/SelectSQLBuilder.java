package cool.scx.data.jdbc.sql_builder;

import cool.scx.common.constant.CharPools;
import cool.scx.data.LockMode;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.EntityTable;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.ArrayUtils.concat;
import static cool.scx.common.util.RandomUtils.randomString;
import static cool.scx.common.util.StringUtils.notEmpty;
import static cool.scx.data.LockMode.EXCLUSIVE;
import static cool.scx.data.LockMode.SHARED;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.filterByQueryFieldPolicy;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.joinWithQuoteIdentifier;
import static cool.scx.jdbc.sql.SQL.sql;

public class SelectSQLBuilder {

    private final EntityTable<?> table;
    private final Dialect dialect;
    private final JDBCWhereParser whereParser;
    private final JDBCOrderByParser orderByParser;

    public SelectSQLBuilder(EntityTable<?> table, Dialect dialect, JDBCWhereParser whereParser, JDBCOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.orderByParser = orderByParser;
    }

    /// 创建虚拟查询列
    public static String[] createVirtualSelectColumns(FieldPolicy fieldPolicy, Dialect dialect) {
        var fieldExpressions = fieldPolicy.getVirtualFields();
        var virtualSelectColumns = new String[fieldExpressions.length];
        int i = 0;
        for (var fieldExpression : fieldExpressions) {
            var fieldName = fieldExpression.virtualFieldName();
            var expression = fieldExpression.expression();
            // 这个虚拟列 因为可能在表中不存在 所以此处不进行名称映射了 直接引用包装一下即可  
            virtualSelectColumns[i] = expression + " AS " + dialect.quoteIdentifier(fieldName);
            i = i + 1;
        }
        return virtualSelectColumns;
    }

    public SQL buildSelectSQL(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        //1, 过滤查询列
        var selectColumns = filterByQueryFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = concat(selectColumns, virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        //7, 创建 SQL
        var sql = GetSelectSQL(finalSelectColumns, whereClause.expression(), orderByClauses, query.getOffset(), query.getLimit(), lockMode);
        return sql(sql, whereClause.params());
    }

    public SQL buildSelectFirstSQL(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        //1, 过滤查询列
        var selectColumns = filterByQueryFieldPolicy(fieldPolicy, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(fieldPolicy, dialect);
        //3, 创建最终查询列
        var finalSelectColumns = concat(selectColumns, virtualSelectColumns);
        //4, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        //7, 创建 SQL
        var sql = GetSelectSQL(finalSelectColumns, whereClause.expression(), orderByClauses, null, 1L, lockMode);
        return sql(sql, whereClause.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    public SQL buildSelectFirstSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        var sql0 = buildSelectFirstSQL(query, fieldPolicy);
        var sql = GetWrapperSelectSQL(sql0.sql());
        return sql(sql, sql0.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    public SQL buildSelectSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        var sql0 = buildSelectSQL(query, fieldPolicy);
        var sql = GetWrapperSelectSQL(sql0.sql());
        return sql(sql, sql0.params());
    }

    public SQL buildSelectSQL(Query query, FieldPolicy fieldPolicy) {
        return buildSelectSQL(query, fieldPolicy, null);
    }

    public SQL buildSelectFirstSQL(Query query, FieldPolicy fieldPolicy) {
        return buildSelectFirstSQL(query, fieldPolicy, null);
    }

    private String GetWrapperSelectSQL(String sql) {
        return "SELECT * FROM (" + sql + ") AS " + getRandomTableName();
    }

    private String GetSelectSQL(Object[] selectColumns, String whereClause, String[] orderByClauses, Long offset, Long limit, LockMode lockMode) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        var sql = "SELECT " + getSelectColumns(selectColumns) + " FROM " + getTableName() + getWhereClause(whereClause) + getOrderByClause(orderByClauses);
        sql = dialect.applyLimit(sql, offset, limit);
        if (lockMode == SHARED) {
            sql = dialect.applySharedLock(sql);
        } else if (lockMode == EXCLUSIVE) {
            sql = dialect.applyExclusiveLock(sql);
        }
        return sql;
    }

    private String getSelectColumns(Object[] selectColumns) {
        return joinWithQuoteIdentifier(selectColumns, dialect);
    }

    private String getTableName() {
        return dialect.quoteIdentifier(table.name());
    }

    private String getRandomTableName() {
        return dialect.quoteIdentifier(table.name() + "_" + randomString(6, CharPools.NUMBER_AND_LOWER_LETTER));
    }

    private String getWhereClause(String whereClause) {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getOrderByClause(String[] orderByClauses) {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

}
