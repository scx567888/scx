package cool.scx.dao.impl;

import cool.scx.dao.*;
import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;
import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;
import cool.scx.sql.SQL;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.result_handler.BeanListHandler;
import cool.scx.sql.result_handler.SingleValueHandler;
import cool.scx.util.RandomUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.dao.SQLBuilder.*;
import static cool.scx.dao.SchemaHelper.findDialect;

/**
 * 最基本的 可以实现 实体类 CRUD 的 DAO
 *
 * @author scx567888
 * @version 0.1.3
 */
public class SQLDao<Entity> implements BaseDao<Entity, Long> {

    /**
     * 实体类对应的 table 结构
     */
    protected final TableInfo<?> tableInfo;

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
     * 查询 count 所用的 handler
     */
    protected final ResultHandler<Long> countResultHandler;

    protected final Dialect dialect;

    /**
     * a
     *
     * @param tableInfo   因为其使用 SQL.ofPlaceholder 的方式进行参数填充 所以必须是 BaseColumnInfo 才可以
     * @param entityClass a
     * @param dataSource  a
     */
    public SQLDao(Class<Entity> entityClass, TableInfo<?> tableInfo, DataSource dataSource) {
        this.entityClass = entityClass;
        this.tableInfo = tableInfo;
        this.sqlRunner = new SQLRunner(dataSource);
        this.dialect = findDialect(dataSource);
        this.entityBeanListHandler = new BeanListHandler<>(BeanBuilder.of(this.entityClass, (field) -> {
            var columnInfo = this.tableInfo.getColumn(field.getName());
            return columnInfo == null ? null : columnInfo.columnName();
        }));
        this.countResultHandler = new SingleValueHandler<>("count", Long.class);
    }

    /**
     * 保存单条数据
     *
     * @param entity       待插入的数据
     * @param updateFilter a
     * @return 插入成功的主键 ID 如果插入失败或数据没有主键则返回 null
     */
    @Override
    public final Long insert(Entity entity, UpdateFilter updateFilter) {
        return sqlRunner.update(_buildInsertSQL(entity, updateFilter)).firstGeneratedKey();
    }

    /**
     * 构建 插入 SQL
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    private SQL _buildInsertSQL(Entity entity, UpdateFilter updateFilter) {
        var insertColumnInfos = updateFilter.filter(entity, tableInfo);
        var insertColumns = Arrays.stream(insertColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new);
        var insertValues = Arrays.stream(insertColumnInfos).map(columnInfo -> "?").toArray(String[]::new);
        var sql = Insert(this.dialect, tableInfo.tableName(), insertColumns)
                .Values(insertValues)
                .GetSQL();
        var objectArray = Arrays.stream(insertColumnInfos).map(c -> c.javaFieldValue(entity)).toArray();
        return SQL.ofPlaceholder(sql, objectArray);
    }

    /**
     * 保存多条数据
     *
     * @param entityList   待保存的列表
     * @param updateFilter a
     * @return 保存成功的主键 (ID) 列表
     */
    @Override
    public final List<Long> insertBatch(Collection<Entity> entityList, UpdateFilter updateFilter) {
        return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, updateFilter)).generatedKeys();
    }

    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    private SQL buildInsertBatchSQL(Collection<Entity> entityList, UpdateFilter updateFilter) {
        var insertColumnInfos = updateFilter.filter(tableInfo);
        //将 entityList 转换为 objectArrayList 这里因为 stream 实在太慢所以改为传统循环方式
        var objectArrayList = new ArrayList<Object[]>();
        for (var entity : entityList) {
            var o = new Object[insertColumnInfos.length];
            for (int i = 0; i < insertColumnInfos.length; i = i + 1) {
                o[i] = insertColumnInfos[i].javaFieldValue(entity);
            }
            objectArrayList.add(o);
        }
        var insertColumns = Arrays.stream(insertColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new);
        var insertValues = Arrays.stream(insertColumnInfos).map(c -> "?").toArray(String[]::new);
        var sql = Insert(this.dialect, tableInfo.tableName(), insertColumns)
                .Values(insertValues)
                .GetSQL();
        return SQL.ofPlaceholder(sql, objectArrayList);
    }

    /**
     * 获取列表
     *
     * @param query        查询过滤条件.
     * @param selectFilter a
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<Entity> select(Query query, SelectFilter selectFilter) {
        return sqlRunner.query(buildSelectSQL(query, selectFilter), entityBeanListHandler);
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
     *                 personService._buildSelectSQL(new Query().lessThan("age", 100), SelectFilter.ofIncluded("carID")),
     *                 SelectFilter.ofExcluded()
     *      ));
     *      // 同时也支持 whereSQL 方法
     *      // 这个写法和上方完全相同
     *      var cars1 = carService._select(new Query().whereSQL("id IN ",
     *                 personService._buildSelectSQL(new Query().lessThan("age", 100), SelectFilter.ofIncluded("carID")),
     *                 SelectFilter.ofExcluded()
     *      ));
     *  }</pre>
     * <br>
     * 注意 !!! 若同时使用 limit 和 in/not in 请使用 {@link SQLDao#buildSelectSQLWithAlias(Query, SelectFilter)}
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return selectSQL
     */
    public final SQL buildSelectSQL(Query query, SelectFilter selectFilter) {
        var selectColumnInfos = selectFilter.filter(tableInfo);
        var selectColumns = Arrays.stream(selectColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new);
        var whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
        var groupByColumns = query.groupBy().getGroupByColumns(tableInfo);
        var orderByClauses = query.orderBy().getOrderByClauses(tableInfo);
        var sql = Select(this.dialect, selectColumns)
                .From(tableInfo.tableName())
                .Where(whereParamsAndWhereClauses.whereClause())
                .GroupBy(groupByColumns)
                .OrderBy(orderByClauses)
                .Limit(query.pagination().offset(), query.pagination().rowCount())
                .GetSQL();
        return SQL.ofPlaceholder(sql, whereParamsAndWhereClauses.whereParams());
    }

    /**
     * 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
     * 此方法便是用于生成嵌套的 sql 的
     *
     * @param query        q
     * @param selectFilter s
     * @return a
     */
    public final SQL buildSelectSQLWithAlias(Query query, SelectFilter selectFilter) {
        //没有 limit 的时候不需要嵌套
        if (query.pagination().rowCount() == null) {
            return buildSelectSQL(query, selectFilter);
        } else {
            var selectColumnInfos = selectFilter.filter(tableInfo);
            var selectColumns = Arrays.stream(selectColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new);
            var whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
            var groupByColumns = query.groupBy().getGroupByColumns(tableInfo);
            var orderByClauses = query.orderBy().getOrderByClauses(tableInfo);
            var sql0 = Select(this.dialect, selectColumns)
                    .From(tableInfo.tableName())
                    .Where(whereParamsAndWhereClauses.whereClause())
                    .GroupBy(groupByColumns)
                    .OrderBy(orderByClauses)
                    .Limit(query.pagination().offset(), query.pagination().rowCount())
                    .GetSQL();
            var sql = Select(this.dialect, "*").From("(" + sql0 + ")").GetSQL();
            return SQL.ofPlaceholder(sql + " AS " + tableInfo.tableName() + "_" + RandomUtils.randomString(6), whereParamsAndWhereClauses.whereParams());
        }
    }

    /**
     * 获取条数
     *
     * @param query 查询条件
     * @return 条数
     */
    @Override
    public final long count(Query query) {
        return sqlRunner.query(buildCountSQL(query), countResultHandler);
    }

    /**
     * 构建 count SQL
     *
     * @param query query 对象
     * @return sql
     */
    private SQL buildCountSQL(Query query) {
        var whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
        var groupByColumns = query.groupBy().getGroupByColumns(tableInfo);
        var sql = Select(this.dialect, "COUNT(*) AS count")
                .From(tableInfo.tableName())
                .Where(whereParamsAndWhereClauses.whereClause())
                .GroupBy(groupByColumns)
                .GetSQL();
        return SQL.ofPlaceholder(sql, whereParamsAndWhereClauses.whereParams());
    }

    /**
     * 更新数据
     *
     * @param entity       要更新的数据
     * @param query        更新的过滤条件
     * @param updateFilter a
     * @return 受影响的条数
     */
    @Override
    public final long update(Entity entity, Query query, UpdateFilter updateFilter) {
        return sqlRunner.update(buildUpdateSQL(entity, query, updateFilter)).affectedItemsCount();
    }

    /**
     * 构建更新 SQL
     *
     * @param entity       待更新的实体
     * @param query        查询条件
     * @param updateFilter filter
     * @return a
     */
    private SQL buildUpdateSQL(Entity entity, Query query, UpdateFilter updateFilter) {
        if (query.where().isEmpty()) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var updateSetColumnInfos = updateFilter.filter(entity, tableInfo);
        var updateSetColumns = Arrays.stream(updateSetColumnInfos).map(c -> c.columnName() + " = ?").toArray(String[]::new);
        var whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
        var sql = Update(this.dialect, tableInfo.tableName())
                .Set(updateSetColumns)
                .Where(whereParamsAndWhereClauses.whereClause())
                .GetSQL();
        var entityParams = Arrays.stream(updateSetColumnInfos).map(c -> c.javaFieldValue(entity)).collect(Collectors.toList());
        entityParams.addAll(List.of(whereParamsAndWhereClauses.whereParams()));
        return SQL.ofPlaceholder(sql, entityParams.toArray());
    }

    /**
     * 删除数据
     *
     * @param query where 条件
     * @return 受影响的条数 (被成功删除的数据条数)
     */
    @Override
    public final long delete(Query query) {
        return sqlRunner.update(buildDeleteSQL(query)).affectedItemsCount();
    }

    /**
     * 构建 删除 SQL
     *
     * @param query query
     * @return sql
     */
    private SQL buildDeleteSQL(Query query) {
        if (query.where().isEmpty()) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
        var sql = Delete(this.dialect, tableInfo.tableName())
                .Where(whereParamsAndWhereClauses.whereClause())
                .GetSQL();
        return SQL.ofPlaceholder(sql, whereParamsAndWhereClauses.whereParams());
    }

    /**
     * 清空表中所有数据 (注意此操作不受事务影响, 所以慎用!!!)
     */
    public final void _truncate() {
        this.sqlRunner.execute(SQL.ofNormal("truncate " + tableInfo.tableName()));
    }

    public final TableInfo<?> _tableInfo() {
        return this.tableInfo;
    }

    @Override
    public final Class<Entity> _entityClass() {
        return this.entityClass;
    }

    public final SQLRunner _sqlRunner() {
        return this.sqlRunner;
    }

}
