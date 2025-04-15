package cool.scx.data.jdbc;

import cool.scx.data.Repository;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.parser.JDBCDaoColumnNameParser;
import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.result_handler.ResultHandler;
import cool.scx.jdbc.sql.SQL;
import cool.scx.jdbc.sql.SQLRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static cool.scx.common.util.ArrayUtils.*;
import static cool.scx.common.util.RandomUtils.randomString;
import static cool.scx.data.jdbc.A.filterByFieldPolicy;
import static cool.scx.data.jdbc.DataJDBCHelper.*;
import static cool.scx.jdbc.result_handler.ResultHandler.*;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.*;

/// 使用 JDBC 接口, 通过 SQL 操作关系型数据库的 DAO
///
/// @author scx567888
/// @version 0.0.1
public class JDBCMapRepository implements Repository<Map<String, Object>, Long> {

    /// 实体类对应的 table 结构
    protected final Table table;

    /// SQLRunner
    protected final SQLRunner sqlRunner;

    /// 实体类对应的 BeanListHandler
    protected final ResultHandler<List<Map<String, Object>>> entityBeanListHandler;

    /// 实体类对应的 BeanListHandler
    protected final ResultHandler<Map<String, Object>> entityBeanHandler;

    /// 查询 count 所用的 handler
    protected final ResultHandler<Long> countResultHandler;

    protected final JDBCDaoColumnNameParser columnNameParser;

    /// where 解析器
    protected final JDBCDaoWhereParser whereParser;

    protected final JDBCDaoGroupByParser groupByParser;

    protected final JDBCDaoOrderByParser orderByParser;

    protected final JDBCContext jdbcContext;

    protected final Function<Field, String> columnNameMapping;

    public JDBCMapRepository(Table tableInfo, JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
        this.sqlRunner = jdbcContext.sqlRunner();
        this.table = tableInfo;
        this.columnNameMapping = field -> {
            var columnInfo = this.table.getColumn(field.getName());
            return columnInfo == null ? null : columnInfo.name();
        };
        this.entityBeanListHandler = ofMapList();
        this.entityBeanHandler = ofMap();
        this.countResultHandler = ofSingleValue("count", Long.class);
        this.columnNameParser = new JDBCDaoColumnNameParser(this.table, jdbcContext.dialect());
        this.whereParser = new JDBCDaoWhereParser(this.columnNameParser);
        this.groupByParser = new JDBCDaoGroupByParser(this.columnNameParser);
        this.orderByParser = new JDBCDaoOrderByParser(this.columnNameParser);
    }

    @Override
    public final Long add(Map<String, Object> entity, FieldPolicy updateFilter) {
        return sqlRunner.update(buildInsertSQL(entity, updateFilter)).firstGeneratedKey();
    }

    @Override
    public final List<Long> add(Collection<Map<String, Object>> entityList, FieldPolicy updateFilter) {
        return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, updateFilter)).generatedKeys();
    }

    @Override
    public final List<Map<String, Object>> find(Query query, FieldPolicy selectFilter) {
        return sqlRunner.query(buildSelectSQL(query, selectFilter), entityBeanListHandler);
    }

    @Override
    public void find(Query query, FieldPolicy fieldFilter, Consumer<Map<String, Object>> consumer) {
        sqlRunner.query(buildSelectSQL(query, fieldFilter), ofMapConsumer(consumer));
    }

    public Map<String, Object> get(Query query, FieldPolicy columnFilter) {
        return sqlRunner.query(buildGetSQL(query, columnFilter), entityBeanHandler);
    }

    @Override
    public final long update(Map<String, Object> entity, Query query, FieldPolicy updateFilter) {
        return sqlRunner.update(buildUpdateSQL(entity, query, updateFilter)).affectedItemsCount();
    }

    @Override
    public final long delete(Query query) {
        return sqlRunner.update(buildDeleteSQL(query)).affectedItemsCount();
    }

    @Override
    public final long count(Query query) {
        return sqlRunner.query(buildCountSQL(query), countResultHandler);
    }

    @Override
    public final void clear() {
        this.sqlRunner.execute(sql("truncate " + table.name()));
    }

    @Override
    public final Class<Map<String, Object>> entityClass() {
        return null;
    }

    public final Table tableInfo() {
        return this.table;
    }

    public final SQLRunner sqlRunner() {
        return this.sqlRunner;
    }

    public ResultHandler<List<Map<String, Object>>> entityBeanListHandler() {
        return entityBeanListHandler;
    }

    public ResultHandler<Map<String, Object>> entityBeanHandler() {
        return entityBeanHandler;
    }

    public JDBCContext jdbcContext() {
        return jdbcContext;
    }

    public SQL buildInsertSQL(Map<String,Object> entity, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByFieldPolicy(fieldPolicy, table, entity);
        //2, 根据 字段策略 创建插入的表达式列
        var insertExpressionsColumns = createInsertExpressionsColumns(fieldPolicy, columnNameParser);
        //3, 创建 插入值 其实都是 '?' 
        var insertValues = createInsertValues(insertColumns);
        //4, 创建 插入表达式
        var insertExpressionsValue = createInsertExpressionsValue(fieldPolicy);
        //5, 拼接最终的 插入列
        var finalInsertColumns = tryConcatAny(insertColumns, (Object[]) insertExpressionsColumns);
        //6, 拼接最终的 插入值
        var finalInsertValues = tryConcat(insertValues, insertExpressionsValue);
        //7, 创建 SQL 语句字符串
        var sql = Insert(table, finalInsertColumns)
                .Values(finalInsertValues)
                .GetSQL(jdbcContext.dialect());
        //8, 提取 entity 中的值作为 SQL 参数
        var params = extractValues(insertColumns, entity);
        return sql(sql, params);
    }

    public SQL buildInsertBatchSQL(Collection<Map<String,Object>> entityList, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByFieldPolicy(fieldPolicy, table);
        //2, 根据 字段策略 创建插入的表达式列
        var insertExpressionsColumns = createInsertExpressionsColumns(fieldPolicy, columnNameParser);
        //3, 创建 插入值 其实都是 '?' 
        var insertValues = createInsertValues(insertColumns);
        //4, 创建 插入表达式
        var insertExpressionsValue = createInsertExpressionsValue(fieldPolicy);
        //5, 拼接最终的 插入列
        var finalInsertColumns = tryConcatAny(insertColumns, (Object[]) insertExpressionsColumns);
        //6, 拼接最终的 插入值
        var finalInsertValues = tryConcat(insertValues, insertExpressionsValue);
        //7, 创建 SQL 语句字符串
        var sql = Insert(table, finalInsertColumns)
                .Values(finalInsertValues)
                .GetSQL(jdbcContext.dialect());
        //8, 提取 entity 中的值作为 SQL 参数
        var batchParams = new Object[entityList.size()];
        var i = 0;
        for (var entity : entityList) {
            batchParams[i] = extractValues(insertColumns, entity);
            i = i + 1;
        }
        return sql(sql, batchParams);
    }

    public SQL buildSelectSQL(Query query, FieldPolicy selectFilter) {
        //1, 过滤查询列
        var selectColumns = filter(selectFilter, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(selectFilter, jdbcContext.dialect());
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
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

    public SQL buildGetSQL(Query query, FieldPolicy selectFilter) {
        //1, 过滤查询列
        var selectColumns = filter(selectFilter, table);
        //2, 创建虚拟查询列
        var virtualSelectColumns = createVirtualSelectColumns(selectFilter, jdbcContext.dialect());
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
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

    public SQL buildUpdateSQL(Entity entity, Query query, FieldPolicy updateFilter) {
        if (query.getWhere().length == 0) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        //1, 过滤需要更新的列
        var updateSetColumns = filterByFieldPolicy(updateFilter, table, entity);
        //2, 创建 set 子句 其实都是 '?'
        var updateSetClauses = createUpdateSetClauses(updateSetColumns, jdbcContext.dialect());
        //3, 创建 表达式 set 子句
        var updateSetExpressionsColumns = createUpdateSetExpressionsClauses(updateFilter, columnNameParser);
        //4, 创建最终的 set 子句
        var finalUpdateSetClauses = tryConcat(updateSetClauses, updateSetExpressionsColumns);
        //5, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        //7, 创建 SQL
        var sql = Update(table)
                .Set(finalUpdateSetClauses)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit())
                .GetSQL(jdbcContext.dialect());
        //8, 提取 entity 参数
        var entityParams = extractValues(updateSetColumns, entity);
        //9, 拼接参数 
        var finalParams = tryConcat(entityParams, whereClause.params());
        return sql(sql, finalParams);
    }

    public SQL buildDeleteSQL(Query query) {
        if (query.getWhere().length == 0) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var whereClause = whereParser.parse(query.getWhere());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var sql = Delete(table)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit())
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

    public SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        var sql = Select("COUNT(*) AS count")
                .From(table)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public SQL buildGetSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var sql0 = buildGetSQL(query, selectFilter);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(jdbcContext.dialect());
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public SQL buildSelectSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var sql0 = buildSelectSQL(query, selectFilter);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(jdbcContext.dialect());
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

}
