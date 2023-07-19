package cool.scx.data.jdbc;

import cool.scx.data.Dao;
import cool.scx.data.Query;
import cool.scx.data.jdbc.mapping.Column;
import cool.scx.data.jdbc.mapping.Table;
import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.jdbc.result_handler.ResultHandler;
import cool.scx.data.jdbc.sql.SQL;
import cool.scx.data.jdbc.sql.SQLRunner;
import cool.scx.data.query.FieldFilter;
import cool.scx.util.RandomUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static cool.scx.data.jdbc.FieldFilterHelper.filter;
import static cool.scx.data.jdbc.result_handler.ResultHandler.*;
import static cool.scx.data.jdbc.sql.SQLBuilder.*;
import static cool.scx.util.ArrayUtils.concat;

/**
 * 使用 JDBC 接口, 通过 SQL 操作关系型数据库的 DAO
 *
 * @author scx567888
 * @version 0.1.3
 */
public class JDBCDao<Entity> implements Dao<Entity, Long> {

    /**
     * 实体类对应的 table 结构
     */
    protected final AnnotationConfigTable tableInfo;

    /**
     * 实体类 class 用于泛型转换
     */
    protected final Class<Entity> entityClass;

    /**
     * SQLRunner
     */
    protected final SQLRunner sqlRunner;

    /**
     * 实体类对应的 BeanListHandler
     */
    protected final ResultHandler<List<Entity>> entityBeanListHandler;

    /**
     * 实体类对应的 BeanListHandler
     */
    protected final ResultHandler<Entity> entityBeanHandler;

    /**
     * 查询 count 所用的 handler
     */
    protected final ResultHandler<Long> countResultHandler;

    /**
     * where 解析器
     */
    protected final JDBCDaoWhereParser whereParser;

    protected final JDBCDaoGroupByParser groupByParser;

    protected final JDBCDaoOrderByParser orderByParser;

    private final JDBCContext jdbcContext;

    /**
     * a
     *
     * @param entityClass a
     */
    public JDBCDao(Class<Entity> entityClass, JDBCContext jdbcContext) {
        this.entityClass = entityClass;
        this.jdbcContext = jdbcContext;
        this.sqlRunner = jdbcContext.sqlRunner();
        this.tableInfo = new AnnotationConfigTable(entityClass);
        var columnNameMapping = (Function<Field, String>) field -> {
            var columnInfo = this.tableInfo.getColumn(field.getName());
            return columnInfo == null ? null : columnInfo.name();
        };
        this.entityBeanListHandler = ofBeanList(this.entityClass, columnNameMapping);
        this.entityBeanHandler = ofBean(this.entityClass, columnNameMapping);
        this.countResultHandler = ofSingleValue("count", Long.class);
        this.whereParser = new JDBCDaoWhereParser(tableInfo, jdbcContext.dialect());
        this.groupByParser = new JDBCDaoGroupByParser(tableInfo);
        this.orderByParser = new JDBCDaoOrderByParser(tableInfo);
    }

    @Override
    public Long add(Entity entity, FieldFilter fieldFilter) {
        return sqlRunner.update(_buildInsertSQL(entity, fieldFilter)).firstGeneratedKey();
    }

    @Override
    public List<Long> addAll(Collection<Entity> entityList, FieldFilter fieldFilter) {
        return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, fieldFilter)).generatedKeys();
    }

    @Override
    public List<Entity> find(Query query) {
        return sqlRunner.query(buildSelectSQL(query), entityBeanListHandler);
    }

    @Override
    public Entity get(Query query) {
        return sqlRunner.query(buildSelectSQL(query.clearOffset().limit(1)), entityBeanHandler);
    }

    @Override
    public long update(Entity entity, Query query) {
        return sqlRunner.update(buildUpdateSQL(entity, query)).affectedItemsCount();
    }

    @Override
    public long delete(Query query) {
        return sqlRunner.update(buildDeleteSQL(query)).affectedItemsCount();
    }

    @Override
    public long count(Query query) {
        return sqlRunner.query(buildCountSQL(query), countResultHandler);
    }

    @Override
    public void _clear() {
        this.sqlRunner.execute(SQL.ofNormal("truncate " + tableInfo.name()));
    }

    @Override
    public Class<Entity> _entityClass() {
        return this.entityClass;
    }

    public final Table<? extends ColumnMapping> _tableInfo() {
        return this.tableInfo;
    }

    public final SQLRunner _sqlRunner() {
        return this.sqlRunner;
    }


    /**
     * 构建 删除 SQL
     *
     * @param query query
     * @return sql
     */
    private SQL buildDeleteSQL(Query query) {
        if (query.getWhere().isEmpty()) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var whereClause = whereParser.parseWhere(query.getWhere());
        var orderByClauses = orderByParser.parseOrderBy(query.getOrderBy());
        var sql = Delete(tableInfo.name())
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit().getLimit())
                .GetSQL(jdbcContext.dialect());
        return SQL.ofPlaceholder(sql, whereClause.params());
    }


    /**
     * 构建更新 SQL
     *
     * @param entity 待更新的实体
     * @param query  查询条件
     * @return a
     */
    private SQL buildUpdateSQL(Entity entity, Query query) {
        if (query.getWhere().isEmpty()) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var updateSetColumnInfos = filter(query.getFieldFilter(), entity, tableInfo);
        var updateSetColumns = Arrays.stream(updateSetColumnInfos).map(c -> c.name() + " = ?").toArray(String[]::new);
        var whereClause = whereParser.parseWhere(query.getWhere());
        var orderByClauses = orderByParser.parseOrderBy(query.getOrderBy());
        var sql = Update(tableInfo.name())
                .Set(updateSetColumns)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit().getLimit())
                .GetSQL(jdbcContext.dialect());
        var entityParams = Arrays.stream(updateSetColumnInfos).map(c -> c.javaFieldValue(entity)).toArray();
        return SQL.ofPlaceholder(sql, concat(entityParams, whereClause.params()));
    }


    /**
     * 构建 count SQL
     *
     * @param query query 对象
     * @return sql
     */
    private SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parseWhere(query.getWhere());
        var groupByColumns = groupByParser.parseGroupBy(query.getGroupBy());
        var sql = Select("COUNT(*) AS count")
                .From(tableInfo.name())
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .GetSQL(jdbcContext.dialect());
        return SQL.ofPlaceholder(sql, whereClause.params());
    }

    /**
     * 构建 (根据聚合查询条件 {@link Query} 获取数据列表) 的SQL
     * <br>
     * 可用于另一条查询语句的 where 条件
     * 用法<pre>{@code
     *      // 假设有以下结构的两个实体类
     *      public class Person {
     *
     *          // ID
     *          public Long id;
     *
     *          // 关联的 汽车 ID
     *          public Long carID;
     *
     *          // 年龄
     *          public Integer age;
     *
     *      }
     *      public class Car {
     *
     *          // ID
     *          public Long id;
     *
     *          // 汽车 名称
     *          public String name;
     *
     *      }
     *      // 现在想做如下查询 根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据
     *      // 可以按照如下写法
     *      var cars = carService._select(new Query().in("id",
     *                 personService._buildSelectSQL(new Query().lessThan("age", 100), ColumnFilter.ofIncluded("carID")),
     *                 ColumnFilter.ofExcluded()
     *      ));
     *      // 同时也支持 whereSQL 方法
     *      // 这个写法和上方完全相同
     *      var cars1 = carService._select(new Query().whereSQL("id IN ",
     *                 personService._buildSelectSQL(new Query().lessThan("age", 100), ColumnFilter.ofIncluded("carID")),
     *                 ColumnFilter.ofExcluded()
     *      ));
     *  }</pre>
     * <br>
     * 注意 !!! 若同时使用 limit 和 in/not in 请使用 {@link JDBCDao#buildSelectSQLWithAlias(Query)}
     *
     * @param query 聚合查询参数对象
     * @return selectSQL
     */
    public final SQL buildSelectSQL(Query query) {
        var selectColumnInfos = filter(query.getFieldFilter(), tableInfo);
        var selectColumns = Arrays.stream(selectColumnInfos).map(Column::name).toArray(String[]::new);
        var whereClause = whereParser.parseWhere(query.getWhere());
        var groupByColumns = groupByParser.parseGroupBy(query.getGroupBy());
        var orderByClauses = orderByParser.parseOrderBy(query.getOrderBy());
        var sql = Select(selectColumns)
                .From(tableInfo.name())
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(query.getLimit().getOffset(), query.getLimit().getLimit())
                .GetSQL(jdbcContext.dialect());
        return SQL.ofPlaceholder(sql, whereClause.params());
    }

    /**
     * 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
     * 此方法便是用于生成嵌套的 sql 的
     *
     * @param query q
     * @return a
     */
    public final SQL buildSelectSQLWithAlias(Query query) {
        //没有 limit 的时候不需要嵌套
        if (query.getLimit().getLimit() == null) {
            return buildSelectSQL(query);
        } else {
            var selectColumnInfos = filter(query.getFieldFilter(), tableInfo);
            var selectColumns = Arrays.stream(selectColumnInfos).map(ColumnMapping::name).toArray(String[]::new);
            var whereClause = whereParser.parseWhere(query.getWhere());
            var groupByColumns = groupByParser.parseGroupBy(query.getGroupBy());
            var orderByClauses = orderByParser.parseOrderBy(query.getOrderBy());
            var sql0 = Select(selectColumns)
                    .From(tableInfo.name())
                    .Where(whereClause.whereClause())
                    .GroupBy(groupByColumns)
                    .OrderBy(orderByClauses)
                    .Limit(query.getLimit().getOffset(), query.getLimit().getLimit())
                    .GetSQL(jdbcContext.dialect());
            var sql = Select("*").From("(" + sql0 + ")").GetSQL(jdbcContext.dialect());
            return SQL.ofPlaceholder(sql + " AS " + tableInfo.name() + "_" + RandomUtils.randomString(6), whereClause.params());
        }
    }


    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    private SQL buildInsertBatchSQL(Collection<? extends Entity> entityList, FieldFilter updateFilter) {
        var insertColumnInfos = filter(updateFilter, tableInfo);
        //将 entityList 转换为 objectArrayList 这里因为 stream 实在太慢所以改为传统循环方式
        var objectArrayList = new ArrayList<Object[]>();
        for (var entity : entityList) {
            var o = new Object[insertColumnInfos.length];
            for (int i = 0; i < insertColumnInfos.length; i = i + 1) {
                o[i] = insertColumnInfos[i].javaFieldValue(entity);
            }
            objectArrayList.add(o);
        }
        var insertColumns = Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new);
        var insertValues = Arrays.stream(insertColumnInfos).map(c -> "?").toArray(String[]::new);
        var sql = Insert(tableInfo.name(), insertColumns)
                .Values(insertValues)
                .GetSQL(jdbcContext.dialect());
        return SQL.ofPlaceholder(sql, objectArrayList);
    }

    /**
     * 构建 插入 SQL
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    private SQL _buildInsertSQL(Entity entity, FieldFilter updateFilter) {
        var insertColumnInfos = filter(updateFilter, entity, tableInfo);
        var insertColumns = Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new);
        var insertValues = Arrays.stream(insertColumnInfos).map(columnInfo -> "?").toArray(String[]::new);
        var sql = Insert(tableInfo.name(), insertColumns)
                .Values(insertValues)
                .GetSQL(jdbcContext.dialect());
        var objectArray = Arrays.stream(insertColumnInfos).map(c -> c.javaFieldValue(entity)).toArray();
        return SQL.ofPlaceholder(sql, objectArray);
    }

}
