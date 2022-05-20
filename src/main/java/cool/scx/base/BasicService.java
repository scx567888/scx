package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;
import cool.scx.dao.ScxDaoTableInfo;
import cool.scx.sql.AbstractPlaceholderSQL;
import cool.scx.sql.PlaceholderSQL;
import cool.scx.sql.SQLBuilder;
import cool.scx.sql.handler.BeanListHandler;
import cool.scx.sql.handler.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 最基本的 可以实现 实体类 CRUD 的 Service
 * 注意和 {@link cool.scx.base.BaseModelService} 进行区分
 *
 * @author scx567888
 * @version 1.5.1
 */
public class BasicService<Entity> {

    /**
     * 实体类对应的 table 结构
     */
    protected final ScxDaoTableInfo scxDaoTableInfo;

    /**
     * 实体类 class 用于泛型转换
     */
    protected final Class<Entity> entityClass;

    /**
     * 实体类对应的 BeanListHandler
     */
    protected final BeanListHandler<Entity> entityBeanListHandler;

    /**
     * 查询 count 所用的 handler
     */
    protected final ScalarHandler<Long> countResultHandler = new ScalarHandler<>("count", Long.class);

    /**
     * 从泛型中获取 entityClass
     */
    @SuppressWarnings("unchecked")
    public BasicService() {
        var genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            this.entityClass = (Class<Entity>) typeArguments[0];
            this.scxDaoTableInfo = new ScxDaoTableInfo(this.entityClass);
            this.entityBeanListHandler = new BeanListHandler<>(this.entityClass);
        } else {
            throw new IllegalArgumentException(this.getClass().getName() + " : 必须设置泛型参数 !!!");
        }
    }

    /**
     * 手动创建 entityClass
     *
     * @param entityClass 继承自 {@link cool.scx.base.BaseModel} 的实体类 class
     */
    public BasicService(Class<Entity> entityClass) {
        this.entityClass = entityClass;
        this.scxDaoTableInfo = new ScxDaoTableInfo(this.entityClass);
        this.entityBeanListHandler = new BeanListHandler<>(this.entityClass);
    }

    /**
     * 保存单条数据
     *
     * @param entity       待插入的数据
     * @param updateFilter a
     * @return 插入成功的主键 ID 如果插入失败或数据没有主键则返回 null
     */
    public final Long _insert(Entity entity, UpdateFilter updateFilter) {
        return ScxContext.sqlRunner().update(_buildInsertSQL(entity, updateFilter)).firstGeneratedKey();
    }

    /**
     * 构建 插入 SQL
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    private AbstractPlaceholderSQL<?> _buildInsertSQL(Entity entity, UpdateFilter updateFilter) {
        var insertColumns = updateFilter.filter(entity, scxDaoTableInfo.columnInfos());
        var sql = SQLBuilder.Insert(scxDaoTableInfo.tableName(), insertColumns).Values(insertColumns).GetSQL();
        return PlaceholderSQL.of(sql, Arrays.stream(insertColumns).map(c -> c.getFieldValue(entity)).toArray());
    }

    /**
     * 保存多条数据
     *
     * @param entityList   待保存的列表
     * @param updateFilter a
     * @return 保存成功的主键 (ID) 列表
     */
    public final List<Long> _insertBatch(Collection<Entity> entityList, UpdateFilter updateFilter) {
        return ScxContext.sqlRunner().updateBatch(_buildInsertBatchSQL(entityList, updateFilter)).generatedKeys();
    }

    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    private AbstractPlaceholderSQL<?> _buildInsertBatchSQL(Collection<Entity> entityList, UpdateFilter updateFilter) {
        var insertColumns = updateFilter.filter(scxDaoTableInfo.columnInfos());
        //将 entityList 转换为 objectArrayList 这里因为 stream 实在太慢所以改为传统循环方式
        var objectArrayList = new ArrayList<Object[]>();
        for (var entity : entityList) {
            var o = new Object[insertColumns.length];
            for (int i = 0; i < insertColumns.length; i = i + 1) {
                o[i] = insertColumns[i].getFieldValue(entity);
            }
            objectArrayList.add(o);
        }
        var sql = SQLBuilder.Insert(scxDaoTableInfo.tableName(), insertColumns).Values(insertColumns).GetSQL();
        return PlaceholderSQL.ofBatch(sql, objectArrayList);
    }

    /**
     * 获取列表
     *
     * @param query        查询过滤条件.
     * @param selectFilter a
     * @return a {@link java.util.List} object.
     */
    public final List<Entity> _select(Query query, SelectFilter selectFilter) {
        return ScxContext.sqlRunner().query(_buildSelectSQL(query, selectFilter), entityBeanListHandler);
    }

    /**
     * 构建查询 SQL
     *
     * @param query        a
     * @param selectFilter a
     * @return a
     */
    private AbstractPlaceholderSQL<?> _buildSelectSQL(Query query, SelectFilter selectFilter) {
        var selectColumnInfos = selectFilter.filter(scxDaoTableInfo.columnInfos());
        var sql = SQLBuilder.Select(selectColumnInfos).From(scxDaoTableInfo.tableName()).Where(query.where()).GroupBy(query.groupBy()).OrderBy(query.orderBy()).Limit(query.pagination()).GetSQL();
        return PlaceholderSQL.of(sql, query.where().getWhereParams());
    }

    /**
     * 获取条数
     *
     * @param query 查询条件
     * @return 条数
     */
    public final long _count(Query query) {
        return ScxContext.sqlRunner().query(_buildCountSQL(query), countResultHandler);
    }

    /**
     * 构建 count SQL
     *
     * @param query query 对象
     * @return sql
     */
    private AbstractPlaceholderSQL<?> _buildCountSQL(Query query) {
        var sql = SQLBuilder.Select("COUNT(*) AS count").From(scxDaoTableInfo.tableName()).Where(query.where()).GroupBy(query.groupBy()).GetSQL();
        return PlaceholderSQL.of(sql, query.where().getWhereParams());
    }

    /**
     * 更新数据
     *
     * @param entity       要更新的数据
     * @param query        更新的过滤条件
     * @param updateFilter a
     * @return 受影响的条数
     */
    public final long _update(Entity entity, Query query, UpdateFilter updateFilter) {
        return ScxContext.sqlRunner().update(_buildUpdateSQL(entity, query, updateFilter)).affectedItemsCount();
    }

    /**
     * 构建更新 SQL
     *
     * @param entity       待更新的实体
     * @param query        查询条件
     * @param updateFilter filter
     * @return a
     */
    private AbstractPlaceholderSQL<?> _buildUpdateSQL(Entity entity, Query query, UpdateFilter updateFilter) {
        if (query.where().isEmpty()) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var updateSetColumnInfos = updateFilter.filter(entity, scxDaoTableInfo.columnInfos());
        var sql = SQLBuilder.Update(scxDaoTableInfo.tableName()).Set(updateSetColumnInfos).Where(query.where()).GetSQL();
        var entityParams = Arrays.stream(updateSetColumnInfos).map(c -> c.getFieldValue(entity)).collect(Collectors.toList());
        entityParams.addAll(List.of(query.where().getWhereParams()));
        return PlaceholderSQL.of(sql, entityParams.toArray());
    }

    /**
     * 删除数据
     *
     * @param query where 条件
     * @return 受影响的条数 (被成功删除的数据条数)
     */
    public final long _delete(Query query) {
        return ScxContext.sqlRunner().update(_buildDeleteSQL(query)).affectedItemsCount();
    }

    /**
     * 构建 删除 SQL
     *
     * @param query query
     * @return sql
     */
    private AbstractPlaceholderSQL<?> _buildDeleteSQL(Query query) {
        if (query.where().isEmpty()) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var sql = SQLBuilder.Delete(scxDaoTableInfo.tableName()).Where(query.where()).GetSQL();
        return PlaceholderSQL.of(sql, query.where().getWhereParams());
    }

    /**
     * 方便冗长的 调用
     *
     * @param handler handler
     */
    public final void autoTransaction(ScxHandlerVE<?> handler) {
        ScxContext.sqlRunner().autoTransaction(handler);
    }

    /**
     * 方便冗长的 调用
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public final <T> T autoTransaction(ScxHandlerVRE<T, ?> handler) {
        return ScxContext.sqlRunner().autoTransaction(handler);
    }

    /**
     * a
     *
     * @return a
     */
    public final ScxDaoTableInfo _scxDaoTableInfo() {
        return scxDaoTableInfo;
    }

}
