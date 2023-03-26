package cool.scx.core.base;

import cool.scx.core.ScxContext;
import cool.scx.dao.Query;
import cool.scx.dao.SelectFilter;
import cool.scx.dao.UpdateFilter;
import cool.scx.dao.impl.AnnotationConfigTableInfo;
import cool.scx.dao.impl.SQLDao;
import cool.scx.sql.SQL;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * 提供一些针对 BaseModel 类型实体类 简单的 CRUD 操作的 service 类
 * <p>
 * 业务 service 可以继承此类 (注意 : 如需要被 beanFactory 扫描到 请标注 {@link cool.scx.core.annotation.ScxService} 注解)
 * <p>
 * 或手动创建 : new BaseModelService()
 * <p>
 * 如果还是无法满足需求, 可以考虑使用 {@link cool.scx.sql.SQLRunner}
 *
 * @author scx567888
 * @version 0.3.6
 */
public class BaseModelService<Entity extends BaseModel> {

    /**
     * BaseDao
     */
    protected final SQLDao<Entity> baseDao;

    /**
     * 从泛型中获取 entityClass
     */
    @SuppressWarnings("unchecked")
    public BaseModelService() {
        var genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            var entityClass = (Class<Entity>) typeArguments[0];
            this.baseDao = new SQLDao<>(entityClass, new AnnotationConfigTableInfo(entityClass), ScxContext.dataSource());
        } else {
            throw new IllegalArgumentException(this.getClass().getName() + " : 必须设置泛型参数 !!!");
        }
    }

    /**
     * 手动创建 entityClass
     *
     * @param entityClass 继承自 {@link BaseModel} 的实体类 class
     */
    public BaseModelService(Class<Entity> entityClass) {
        this.baseDao = new SQLDao<>(entityClass, new AnnotationConfigTableInfo(entityClass), ScxContext.dataSource());
    }

    /**
     * 处理 updateFilter  使在插入或更新数据时永远过滤 "id", "dateCreated", "dateUpdated" 三个字段
     *
     * @param updateFilter u
     * @return a {@link UpdateFilter} object
     */
    private static UpdateFilter updateFilterProcessor(UpdateFilter updateFilter) {
        return updateFilter.addExcluded("id", "createdDate", "updatedDate");
    }

    /**
     * 插入数据 (注意 !!! 这里会在插入之后根据主键再次进行一次查询, 若只是进行插入且对性能有要求请使用 {@link SQLDao#insert(Object, UpdateFilter)})
     *
     * @param entity 待插入的数据
     * @return 插入成功的数据 如果插入失败或数据没有主键则返回 null
     */
    public final Entity add(Entity entity) {
        return add(entity, UpdateFilter.ofExcluded());
    }

    /**
     * 插入数据 (注意 !!! 这里会在插入之后根据主键再次进行一次查询, 若只是进行插入且对性能有要求请使用 {@link SQLDao#insert(Object, UpdateFilter)})
     *
     * @param entity       待插入的数据
     * @param updateFilter 更新字段过滤器
     * @return 插入成功的数据 如果插入失败或数据没有主键则返回 null
     */
    public Entity add(Entity entity, UpdateFilter updateFilter) {
        var newID = baseDao.insert(entity, updateFilterProcessor(updateFilter));
        return newID != null ? this.get(newID) : null;
    }

    /**
     * 批量插入数据
     *
     * @param entityList 数据集合
     * @return 插入成功的数据的自增主键列表
     */
    public final List<Long> add(Collection<Entity> entityList) {
        //此处没有设置 f
        return add(entityList, UpdateFilter.ofExcluded());
    }

    /**
     * 批量插入数据
     *
     * @param entityList   数据集合
     * @param updateFilter 更新字段过滤器
     * @return 插入成功的数据的自增主键列表
     */
    public List<Long> add(Collection<Entity> entityList, UpdateFilter updateFilter) {
        return baseDao.insertBatch(entityList, updateFilterProcessor(updateFilter));
    }

    /**
     * 获取所有数据
     *
     * @return 所有数据
     */
    public final List<Entity> list() {
        return list(SelectFilter.ofExcluded());
    }

    /**
     * 获取所有数据 (使用查询过滤器)
     *
     * @param selectFilter 查询字段过滤器
     * @return 所有数据
     */
    public final List<Entity> list(SelectFilter selectFilter) {
        return list(new Query(), selectFilter);
    }

    /**
     * 根据 id 获取数据
     *
     * @param ids id 列表
     * @return 列表数据
     */
    public final List<Entity> list(long... ids) {
        return list(ids.length == 1 ? new Query().equal("id", ids[0]) : new Query().in("id", ids));
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据列表
     *
     * @param query 聚合查询参数对象
     * @return 数据列表
     */
    public final List<Entity> list(Query query) {
        return list(query, SelectFilter.ofExcluded());
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据列表
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return 数据列表
     */
    public List<Entity> list(Query query, SelectFilter selectFilter) {
        return baseDao.select(query, selectFilter);
    }

    /**
     * 根据 ID (主键) 查询单条数据
     *
     * @param id id ( 主键 )
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public final Entity get(long id) {
        return get(id, SelectFilter.ofExcluded());
    }

    /**
     * 根据 ID (主键) 查询单条数据
     *
     * @param id           id ( 主键 )
     * @param selectFilter 查询字段过滤器
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public final Entity get(long id, SelectFilter selectFilter) {
        return get(new Query().equal("id", id), selectFilter);
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取单条数据
     *
     * @param query 聚合查询参数对象
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public final Entity get(Query query) {
        return get(query, SelectFilter.ofExcluded());
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取单条数据
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public final Entity get(Query query, SelectFilter selectFilter) {
        var list = list(query.setPagination(1), selectFilter);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取所有数据的条数
     *
     * @return 所有数据的条数
     */
    public final long count() {
        return count(new Query());
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据条数
     *
     * @param query 聚合查询参数对象
     * @return 数据条数
     */
    public final long count(Query query) {
        return baseDao.count(query);
    }

    /**
     * 根据 ID 更新 (注意 !!! 这里会在更新之后根据主键再次进行一次查询, 若只是进行更新且对性能有要求请使用 {@link SQLDao#update(Object, Query, UpdateFilter)})
     *
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     */
    public final Entity update(Entity entity) {
        return update(entity, UpdateFilter.ofExcluded());
    }

    /**
     * 根据 ID 更新 (注意 !!! 这里会在更新之后根据主键再次进行一次查询, 若只是进行更新且对性能有要求请使用 {@link SQLDao#update(Object, Query, UpdateFilter)})
     *
     * @param entity       待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @param updateFilter 更新字段过滤器
     * @return 更新成功后的数据
     */
    public final Entity update(Entity entity, UpdateFilter updateFilter) {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        this.update(entity, new Query().equal("id", entity.id), updateFilter);
        return this.get(entity.id);
    }

    /**
     * 根据指定条件更新数据
     *
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     */
    public final long update(Entity entity, Query query) {
        return update(entity, query, UpdateFilter.ofExcluded());
    }

    /**
     * 根据指定条件更新数据
     *
     * @param entity       待更新的数据
     * @param query        更新的条件
     * @param updateFilter 更新字段过滤器
     * @return 更新成功的数据条数
     */
    public long update(Entity entity, Query query, UpdateFilter updateFilter) {
        return baseDao.update(entity, query, updateFilterProcessor(updateFilter));
    }

    /**
     * 根据 ID 列表删除指定的数据
     *
     * @param ids 要删除的数据的 id 集合
     * @return 删除成功的数据条数
     */
    public final long delete(long... ids) {
        if (ids.length == 0) {
            throw new IllegalArgumentException("待删除的 ids 数量至少为 1 个");
        }
        return delete(ids.length == 1 ? new Query().equal("id", ids[0]) : new Query().in("id", ids));
    }

    /**
     * 根据条件删除
     *
     * @param query 删除条件
     * @return 被删除的数据条数
     */
    public long delete(Query query) {
        return baseDao.delete(query);
    }

    /**
     * 构建 (根据聚合查询条件 {@link Query} 获取数据列表) 的SQL
     * <br>
     * 可用于另一条查询语句的 where 条件
     * <br>
     * 若同时使用 limit 和 in/not in 请使用 {@link BaseModelService#buildListSQLWithAlias(Query, SelectFilter)}
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return listSQL
     * @see SQLDao#buildSelectSQL(Query, SelectFilter)
     */
    public final SQL buildListSQL(Query query, SelectFilter selectFilter) {
        return baseDao.buildSelectSQL(query, selectFilter);
    }

    /**
     * 构建 根据聚合查询条件 {@link Query} 获取单条数据 的SQL
     * <br>
     * 可用于另一条查询语句的 where 条件
     * <br>
     * 若同时使用 limit 和 in/not in 请使用 {@link BaseModelService#buildListSQLWithAlias(Query, SelectFilter)}
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return getSQL
     * @see SQLDao#buildSelectSQL(Query, SelectFilter)
     */
    public final SQL buildGetSQL(Query query, SelectFilter selectFilter) {
        return buildListSQL(query.setPagination(1), selectFilter);
    }

    /**
     * 构建 (根据聚合查询条件 {@link Query} 获取数据列表) 的SQL
     * <br>
     * 可用于另一条查询语句的 where 条件
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return listSQL
     * @see SQLDao#buildSelectSQL(Query, SelectFilter)
     */
    public final SQL buildListSQLWithAlias(Query query, SelectFilter selectFilter) {
        return baseDao.buildSelectSQLWithAlias(query, selectFilter);
    }

    /**
     * 构建 根据聚合查询条件 {@link Query} 获取单条数据 的SQL
     * <br>
     * 可用于另一条查询语句的 where 条件
     *
     * @param query        聚合查询参数对象
     * @param selectFilter 查询字段过滤器
     * @return getSQL
     * @see SQLDao#buildSelectSQL(Query, SelectFilter)
     */
    public final SQL buildGetSQLWithAlias(Query query, SelectFilter selectFilter) {
        return buildListSQLWithAlias(query.setPagination(1), selectFilter);
    }

    /**
     * <p>baseDao.</p>
     *
     * @return a {@link SQLDao} object
     */
    public SQLDao<Entity> _baseDao() {
        return baseDao;
    }

}
