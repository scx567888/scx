package cool.scx.data.jdbc;

import cool.scx.common.util.RandomUtils;
import cool.scx.data.Repository;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.parser.JDBCDaoColumnNameParser;
import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.query.Query;
import cool.scx.data.query.WhereClause;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.result_handler.ResultHandler;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;
import cool.scx.jdbc.sql.SQL;
import cool.scx.jdbc.sql.SQLRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static cool.scx.common.util.ArrayUtils.concat;
import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.data.jdbc.JDBCDaoHelper.*;
import static cool.scx.jdbc.result_handler.ResultHandler.*;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.*;

/// 使用 JDBC 接口, 通过 SQL 操作关系型数据库的 DAO
///
/// @author scx567888
/// @version 0.0.1
public class JDBCEntityRepository<Entity> implements Repository<Entity, Long> {

    /// 实体类对应的 table 结构
    protected final AnnotationConfigTable tableInfo;

    /// 实体类 class 用于泛型转换
    protected final Class<Entity> entityClass;

    /// SQLRunner
    protected final SQLRunner sqlRunner;

    /// 实体类对应的 BeanListHandler
    protected final ResultHandler<List<Entity>> entityBeanListHandler;

    /// 实体类对应的 BeanListHandler
    protected final ResultHandler<Entity> entityBeanHandler;

    /// 查询 count 所用的 handler
    protected final ResultHandler<Long> countResultHandler;

    protected final JDBCDaoColumnNameParser columnNameParser;

    /// where 解析器
    protected final JDBCDaoWhereParser whereParser;

    protected final JDBCDaoGroupByParser groupByParser;

    protected final JDBCDaoOrderByParser orderByParser;

    protected final JDBCContext jdbcContext;

    protected final Function<Field, String> columnNameMapping;

    protected final BeanBuilder<Entity> beanBuilder;

    /// a
    ///
    /// @param entityClass a
    public JDBCEntityRepository(Class<Entity> entityClass, JDBCContext jdbcContext) {
        this.entityClass = entityClass;
        this.jdbcContext = jdbcContext;
        this.sqlRunner = jdbcContext.sqlRunner();
        this.tableInfo = new AnnotationConfigTable(entityClass);
        this.columnNameMapping = field -> {
            var columnInfo = this.tableInfo.getColumn(field.getName());
            return columnInfo == null ? null : columnInfo.name();
        };
        this.beanBuilder = BeanBuilder.of(this.entityClass, columnNameMapping);
        this.entityBeanListHandler = ofBeanList(this.beanBuilder);
        this.entityBeanHandler = ofBean(this.beanBuilder);
        this.countResultHandler = ofSingleValue("count", Long.class);
        this.columnNameParser = new JDBCDaoColumnNameParser(this.tableInfo, jdbcContext.dialect());
        this.whereParser = new JDBCDaoWhereParser(this.columnNameParser);
        this.groupByParser = new JDBCDaoGroupByParser(this.columnNameParser);
        this.orderByParser = new JDBCDaoOrderByParser(this.columnNameParser);
    }

    @Override
    public final Long add(Entity entity, FieldPolicy updateFilter) {
        return sqlRunner.update(buildInsertSQL(entity, updateFilter)).firstGeneratedKey();
    }

    @Override
    public final List<Long> add(Collection<Entity> entityList, FieldPolicy updateFilter) {
        return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, updateFilter)).generatedKeys();
    }

    @Override
    public final List<Entity> find(Query query, FieldPolicy selectFilter) {
        return sqlRunner.query(buildSelectSQL(query, selectFilter), entityBeanListHandler);
    }

    @Override
    public void find(Query query, FieldPolicy fieldFilter, Consumer<Entity> consumer) {
        sqlRunner.query(buildSelectSQL(query, fieldFilter), ofBeanConsumer(beanBuilder, consumer));
    }

    public Entity get(Query query, FieldPolicy columnFilter) {
        return sqlRunner.query(buildGetSQL(query, columnFilter), entityBeanHandler);
    }

    @Override
    public final long update(Entity entity, Query query, FieldPolicy updateFilter) {
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
        this.sqlRunner.execute(sql("truncate " + tableInfo.name()));
    }

    @Override
    public final Class<Entity> entityClass() {
        return this.entityClass;
    }

    public final AnnotationConfigTable tableInfo() {
        return this.tableInfo;
    }

    public final SQLRunner sqlRunner() {
        return this.sqlRunner;
    }

    public BeanBuilder<Entity> beanBuilder() {
        return beanBuilder;
    }

    public ResultHandler<List<Entity>> entityBeanListHandler() {
        return entityBeanListHandler;
    }

    public ResultHandler<Entity> entityBeanHandler() {
        return entityBeanHandler;
    }

    private String _buildInsertSQL0(Column[] insertColumns) {
        var insertValues = createInsertValues(insertColumns);
        return Insert(tableInfo, insertColumns)
                .Values(insertValues)
                .GetSQL(jdbcContext.dialect());
    }

    private SQL buildInsertSQL(Entity entity, FieldPolicy updateFilter) {
        var insertColumnInfos = filter(updateFilter, entity, tableInfo);
        var sql = _buildInsertSQL0(insertColumnInfos);
        var objectArray = extractValues(insertColumnInfos, entity);
        return sql(sql, objectArray);
    }

    private SQL buildInsertBatchSQL(Collection<? extends Entity> entityList, FieldPolicy updateFilter) {
        var insertColumnInfos = filter(updateFilter, tableInfo);
        //将 entityList 转换为 objectArrayList 这里因为 stream 实在太慢所以改为传统循环方式
        var objectArrayList = new ArrayList<Object[]>();
        for (var entity : entityList) {
            objectArrayList.add(extractValues(insertColumnInfos, entity));
        }
        var sql = _buildInsertSQL0(insertColumnInfos);
        return sql(sql, objectArrayList);
    }

    private String _buildSelectSQL0(Query query, FieldPolicy selectFilter, WhereClause whereClause) {
        var selectColumns = filter(selectFilter, tableInfo);
        Object[] selectVirtualColumns = getVirtualColumns(selectFilter);
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var finalSelectColumns = tryConcatAny(selectColumns, selectVirtualColumns);
        return Select(finalSelectColumns)
                .From(tableInfo)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(query.getOffset(), query.getLimit())
                .GetSQL(jdbcContext.dialect());
    }

    /// 构建 (根据聚合查询条件 [Query] 获取数据列表) 的SQL
    ///
    /// 可用于另一条查询语句的 where 条件
    /// 用法<pre>
    /// `// 假设有以下结构的两个实体类public class Person{// IDpublic Long id;// 关联的 汽车 IDpublic Long carID;// 年龄public Integer age;}public class Car{// IDpublic Long id;// 汽车 名称public String name;}// 现在想做如下查询 根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据// 可以按照如下写法var cars = carService._select(new Query().in("id",personService._buildSelectSQL(new Query().lessThan("age", 100), ColumnFilter.ofIncluded("carID")),ColumnFilter.ofExcluded()));// 同时也支持 whereSQL 方法// 这个写法和上方完全相同var cars1 = carService._select(new Query().whereSQL("id IN ",personService._buildSelectSQL(new Query().lessThan("age", 100), ColumnFilter.ofIncluded("carID")),ColumnFilter.ofExcluded()));`</pre>
    ///
    /// 注意 !!! 若同时使用 limit 和 in/not in 请使用 [#buildSelectSQLWithAlias(Query, FieldPolicy)]
    ///
    /// @param query        聚合查询参数对象
    /// @param selectFilter 查询字段过滤器
    /// @return selectSQL
    public final SQL buildSelectSQL(Query query, FieldPolicy selectFilter) {
        var whereClause = whereParser.parse(query.getWhere());
        var sql = _buildSelectSQL0(query, selectFilter, whereClause);
        return sql(sql, whereClause.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public final SQL buildSelectSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var whereClause = whereParser.parse(query.getWhere());
        var sql0 = _buildSelectSQL0(query, selectFilter, whereClause);
        var sql = Select("*")
                .From("(" + sql0 + ")")
                .GetSQL(jdbcContext.dialect());
        return sql(sql + " AS " + tableInfo.name() + "_" + RandomUtils.randomString(6), whereClause.params());
    }

    private String _buildGetSQL0(Query query, FieldPolicy selectFilter, WhereClause whereClause) {
        var selectColumns = filter(selectFilter, tableInfo);
        Object[] selectVirtualColumns = getVirtualColumns(selectFilter);
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var finalSelectColumns = tryConcatAny(selectColumns, selectVirtualColumns);
        return Select(finalSelectColumns)
                .From(tableInfo)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(null, 1L)
                .GetSQL(jdbcContext.dialect());
    }

    public final SQL buildGetSQL(Query query, FieldPolicy selectFilter) {
        var whereClause = whereParser.parse(query.getWhere());
        var sql = _buildGetSQL0(query, selectFilter, whereClause);
        return sql(sql, whereClause.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public final SQL buildGetSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var whereClause = whereParser.parse(query.getWhere());
        var sql0 = _buildGetSQL0(query, selectFilter, whereClause);
        var sql = Select("*")
                .From("(" + sql0 + ")")
                .GetSQL(jdbcContext.dialect());
        return sql(sql + " AS " + tableInfo.name() + "_" + RandomUtils.randomString(6), whereClause.params());
    }

    private SQL buildUpdateSQL(Entity entity, Query query, FieldPolicy updateFilter) {
        if (query.getWhere().length == 0) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var updateSetColumnInfos = filter(updateFilter, entity, tableInfo);
        var updateSetColumns = createUpdateSetColumns(updateSetColumnInfos, jdbcContext.dialect());
        var whereClause = whereParser.parse(query.getWhere());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var sql = Update(tableInfo)
                .Set(updateSetColumns)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit())
                .GetSQL(jdbcContext.dialect());
        var entityParams = extractValues(updateSetColumnInfos, entity);
        return sql(sql, concat(entityParams, whereClause.params()));
    }

    private SQL buildDeleteSQL(Query query) {
        if (query.getWhere().length == 0) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var whereClause = whereParser.parse(query.getWhere());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var sql = Delete(tableInfo)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit())
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

    private SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        var sql = Select("COUNT(*) AS count")
                .From(tableInfo)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .GetSQL(jdbcContext.dialect());
        return sql(sql, whereClause.params());
    }

}
