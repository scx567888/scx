package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.sql.where.WhereOption;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供一些针对 BaseModel 类型实体类 简单的 CRUD 操作的 service 类
 * <p>
 * 业务 service 可以继承此类 (注意 : 如需要被 beanFactory 扫描到 请标注 {@link cool.scx.annotation.ScxService} 注解)
 * <p>
 * 或手动创建 : new BaseModelService()
 * <p>
 * 注意和 {@link BasicService} 进行区分
 * <p>
 * '_' 下划线开头的方法为 BasicService 的实现方法, 其余为基于以上方法进行的封装以便使用
 * <p>
 * 如果还是无法满足需求, 可以考虑使用 {@link cool.scx.sql.SQLRunner}
 *
 * @author scx567888
 * @version 0.3.6
 */
public class BaseModelService<Entity extends BaseModel> extends BasicService<Entity> {

    /**
     * a
     */
    public BaseModelService() {
        super();
    }

    /**
     * a
     *
     * @param entityClass a
     */
    public BaseModelService(Class<Entity> entityClass) {
        super(entityClass);
    }

    /**
     * 处理墓碑机制的数据
     * <br>
     * 如果没有开启墓碑机制 : 不做任何处理
     * <br>
     * 如果开启墓碑机制 做以下处理
     * 1, 在查询条件强制添加 tombstone 字段值等于 false
     * <br>
     * 2, 根据不同的 selectFilter 类型进行查询参数过滤 隐藏数据库中所有 tombstone 字段的信息
     *
     * @param query q
     */
    private static Query queryProcessorForTombstone(Query query) {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return query;
    }

    /**
     * 当 启用逻辑删除时 则不允许查询 tombstone 值 所以再次进行处理
     *
     * @param selectFilter s
     */
    private static SelectFilter selectFilterProcessorForTombstone(SelectFilter selectFilter) {
        if (ScxContext.easyConfig().tombstone()) {
            if (selectFilter.filterMode() == AbstractFilter.FilterMode.EXCLUDED) {
                selectFilter.add("tombstone");
            } else if (selectFilter.filterMode() == AbstractFilter.FilterMode.INCLUDED) {
                selectFilter.remove("tombstone");
            }
        }
        return selectFilter;
    }

    /**
     * 处理 updateFilter  使在插入数据时永远过滤 "id", "updateDate", "createDate", "tombstone" 四个字段
     *
     * @param updateFilter u
     */
    private static UpdateFilter updateFilterProcessorWhenSave(UpdateFilter updateFilter) {
        if (updateFilter.filterMode() == AbstractFilter.FilterMode.EXCLUDED) {
            updateFilter.add("id", "updateDate", "createDate", "tombstone");
        } else if (updateFilter.filterMode() == AbstractFilter.FilterMode.INCLUDED) {
            updateFilter.remove("id", "updateDate", "createDate", "tombstone");
        }
        return updateFilter;
    }

    /**
     * 处理 updateFilter  使在插入数据时永远过滤 "id", "updateDate", "createDate", "tombstone" 四个字段
     *
     * @param updateFilter u
     */
    private static UpdateFilter updateFilterProcessorWhenUpdate(UpdateFilter updateFilter) {
        if (updateFilter.filterMode() == AbstractFilter.FilterMode.EXCLUDED) {
            updateFilter.add("id", "updateDate", "createDate");
        } else if (updateFilter.filterMode() == AbstractFilter.FilterMode.INCLUDED) {
            updateFilter.remove("id", "updateDate", "createDate");
        }
        return updateFilter;
    }

    /**
     * 插入数据
     *
     * @param entity 待插入的数据
     * @return 插入后的数据
     */
    public Entity save(Entity entity) {
        //此处使用一个默认的 UpdateFilter 用来过滤实体类中为空的字段
        return save(entity, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    public Entity save(Entity entity, UpdateFilter updateFilter) {
        var newId = this._insert(entity, updateFilterProcessorWhenSave(updateFilter));
        return this.get(newId);
    }

    /**
     * 批量插入数据
     *
     * @param entityList 数据集合
     * @return 插入成功的数据的自增主键列表
     */
    public List<Long> save(List<Entity> entityList) {
        //此处没有设置 f
        return save(entityList, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    public List<Long> save(List<Entity> entityList, UpdateFilter updateFilter) {
        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<>();
        } else {
            return this._insertBatch(entityList, updateFilterProcessorWhenSave(updateFilter));
        }
    }

    /**
     * 插入数据 (不使用自动提交)
     *
     * @param entity 待插入的数据
     * @param con    a {@link java.sql.Connection} object
     * @return 插入后的数据
     * @throws java.sql.SQLException if any.
     */
    public Entity save(Connection con, Entity entity) throws SQLException {
        return save(con, entity, UpdateFilter.ofExcluded());
    }

    /**
     * 插入数据 (不使用自动提交)
     *
     * @param entity       待插入的数据
     * @param con          a {@link java.sql.Connection} object
     * @param updateFilter u
     * @return 插入后的数据
     * @throws java.sql.SQLException if any.
     */
    public Entity save(Connection con, Entity entity, UpdateFilter updateFilter) throws SQLException {
        var newId = this._insert(con, entity, updateFilterProcessorWhenSave(updateFilter));
        return this.get(con, newId);
    }

    /**
     * 批量插入数据 (不使用自动提交)
     *
     * @param entityList 数据集合
     * @param con        a {@link java.sql.Connection} object
     * @return 插入成功的数据的自增主键列表
     * @throws java.sql.SQLException if any.
     */
    public List<Long> save(Connection con, List<Entity> entityList) throws SQLException {
        return save(con, entityList, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param entityList   a
     * @param updateFilter a
     * @return a
     * @throws SQLException a
     */
    public List<Long> save(Connection con, List<Entity> entityList, UpdateFilter updateFilter) throws SQLException {
        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<>();
        } else {
            return this._insertBatch(con, entityList, updateFilterProcessorWhenSave(updateFilter));
        }
    }

    /**
     * 获取所有数据 (注意 : 默认根据最后更新时间 {@link cool.scx.base.BaseModel#updateDate} 排序)
     *
     * @return 所有数据
     */
    public List<Entity> list() {
        return list(SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param selectFilter a
     * @return a
     */
    public List<Entity> list(SelectFilter selectFilter) {
        return list(new Query().desc("updateDate"), selectFilter);
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据列表
     *
     * @param query 聚合查询参数对象
     * @return 数据列表
     */
    public List<Entity> list(Query query) {
        return list(query, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param query        a
     * @param selectFilter a
     * @return a
     */
    public List<Entity> list(Query query, SelectFilter selectFilter) {
        return this._select(queryProcessorForTombstone(query), selectFilterProcessorForTombstone(selectFilter));
    }

    /**
     * a
     *
     * @param con a {@link java.sql.Connection} object
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con) throws SQLException {
        return list(con, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a  {@link java.sql.Connection} object
     * @param selectFilter a
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con, SelectFilter selectFilter) throws SQLException {
        return list(con, new Query().desc("updateDate"), selectFilter);
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据列表
     *
     * @param con   a {@link java.sql.Connection} object
     * @param query 聚合查询参数对象
     * @return 数据列表
     * @throws SQLException s
     */
    public List<Entity> list(Connection con, Query query) throws SQLException {
        return list(con, query, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param query        a
     * @param selectFilter a
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con, Query query, SelectFilter selectFilter) throws SQLException {
        return this._select(con, queryProcessorForTombstone(query), selectFilterProcessorForTombstone(selectFilter));
    }

    /**
     * 根据 ID (主键) 查询单条数据
     *
     * @param id id ( 主键 )
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public Entity get(long id) {
        return get(id, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param id           a
     * @param selectFilter a
     * @return a
     */
    public Entity get(long id, SelectFilter selectFilter) {
        return get(new Query().equal("id", id), selectFilter);
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取单条数据
     *
     * @param query 聚合查询参数对象
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public Entity get(Query query) {
        return get(query, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param query        a
     * @param selectFilter a
     * @return a
     */
    public Entity get(Query query, SelectFilter selectFilter) {
        var list = list(query.setPagination(1), selectFilter);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 根据 ID (主键) 查询单条数据
     *
     * @param con a {@link java.sql.Connection} object
     * @param id  id ( 主键 )
     * @return 查到多个则返回第一个 没有则返回 null
     * @throws SQLException e
     */
    public Entity get(Connection con, long id) throws SQLException {
        return get(con, id, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param id           a
     * @param selectFilter a
     * @return a
     * @throws SQLException a
     */
    public Entity get(Connection con, long id, SelectFilter selectFilter) throws SQLException {
        return get(con, new Query().equal("id", id), selectFilter);
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取单条数据
     *
     * @param con   a {@link java.sql.Connection} object
     * @param query 聚合查询参数对象
     * @return 查到多个则返回第一个 没有则返回 null
     * @throws SQLException s
     */
    public Entity get(Connection con, Query query) throws SQLException {
        return get(con, query, SelectFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param query        a
     * @param selectFilter a
     * @return a
     * @throws SQLException a
     */
    public Entity get(Connection con, Query query, SelectFilter selectFilter) throws SQLException {
        var list = list(con, query.setPagination(1), selectFilter);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取所有数据的条数
     *
     * @return 所有数据的条数
     */
    public long count() {
        return count(new Query());
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据条数
     *
     * @param query 聚合查询参数对象
     * @return 数据条数
     */
    public long count(Query query) {
        return this._count(queryProcessorForTombstone(query));
    }

    /**
     * 获取所有数据的条数
     *
     * @param con a {@link java.sql.Connection} object
     * @return 所有数据的条数
     * @throws SQLException s
     */
    public long count(Connection con) throws SQLException {
        return count(con, new Query());
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据条数
     *
     * @param con   a {@link java.sql.Connection} object
     * @param query 聚合查询参数对象
     * @return 数据条数
     * @throws SQLException s
     */
    public long count(Connection con, Query query) throws SQLException {
        return this._count(con, queryProcessorForTombstone(query));
    }

    /**
     * 根据  id 更新
     *
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     */
    public Entity update(Entity entity) {
        return update(entity, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    public Entity update(Entity entity, UpdateFilter updateFilter) {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.update(entity, new Query().equal("id", entity.id), updateFilter);
        return l == 1 ? this.get(entity.id) : null;
    }

    /**
     * 根据指定条件更新数据
     *
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     */
    public long update(Entity entity, Query query) {
        return update(entity, query, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param entity       a
     * @param query        a
     * @param updateFilter a
     * @return a
     */
    public long update(Entity entity, Query query, UpdateFilter updateFilter) {
        //更新成功的条数
        return this._update(entity, queryProcessorForTombstone(query), updateFilterProcessorWhenUpdate(updateFilter));
    }

    /**
     * 根据  id 更新
     *
     * @param con    a {@link java.sql.Connection} object
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     * @throws SQLException s
     */
    public Entity update(Connection con, Entity entity) throws SQLException {
        return update(con, entity, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param entity       a
     * @param updateFilter a
     * @return a
     * @throws SQLException a
     */
    public Entity update(Connection con, Entity entity, UpdateFilter updateFilter) throws SQLException {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.update(con, entity, new Query().equal("id", entity.id), updateFilter);
        return l == 1 ? this.get(entity.id) : null;
    }

    /**
     * 根据指定条件更新数据
     *
     * @param con    a {@link java.sql.Connection} object
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     * @throws SQLException s
     */
    public long update(Connection con, Entity entity, Query query) throws SQLException {
        return update(con, entity, query, UpdateFilter.ofExcluded());
    }

    /**
     * a
     *
     * @param con          a {@link java.sql.Connection} object
     * @param entity       a
     * @param query        a
     * @param updateFilter a
     * @return a
     * @throws SQLException s
     */
    public long update(Connection con, Entity entity, Query query, UpdateFilter updateFilter) throws SQLException {
        //更新成功的条数
        return this._update(con, entity, queryProcessorForTombstone(query), updateFilterProcessorWhenUpdate(updateFilter));
    }

    /**
     * 根据 ID 列表删除指定的数据
     *
     * @param ids 要删除的数据的 id 集合
     * @return 删除成功的数据条数
     */
    public long delete(long... ids) {
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
        //物理删除
        if (!ScxContext.easyConfig().tombstone()) {
            return this._delete(query);
        } else {//逻辑删除
            var needTombstoneEntity = ScxContext.getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            //关于 query 字段 :  tombstone 已经为 false 的不需要在进行处理了所以添加一个排除
            //关于 updateFilter : 这里已经明确 实体类的所需字段不为空 所以为了性能此处 UpdateFilter 关闭 excludeIfFieldValueIsNull 功能
            return this._update(needTombstoneEntity, query.equal("tombstone", false, WhereOption.REPLACE), UpdateFilter.ofIncluded(false).add("tombstone"));
        }
    }

    /**
     * 根据 ID 列表删除指定的数据
     *
     * @param con a {@link java.sql.Connection} object
     * @param ids 要删除的数据的 id 集合
     * @return 删除成功的数据条数
     * @throws SQLException e
     */
    public long delete(Connection con, long... ids) throws SQLException {
        return delete(con, new Query().in("id", ids));
    }

    /**
     * 根据条件删除
     *
     * @param con   a {@link java.sql.Connection} object
     * @param query 删除条件
     * @return 被删除的数据条数
     * @throws SQLException e
     */
    public long delete(Connection con, Query query) throws SQLException {
        //物理删除
        if (!ScxContext.easyConfig().tombstone()) {
            return this._delete(con, query);
        } else {//逻辑删除
            var needTombstoneEntity = ScxContext.getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            //关于 query 字段 :  tombstone 已经为 false 的不需要在进行处理了所以添加一个排除
            //关于 updateFilter : 这里已经明确 实体类的所需字段不为空 所以为了性能此处 UpdateFilter 关闭 excludeIfFieldValueIsNull 功能
            return this._update(con, needTombstoneEntity, query.equal("tombstone", false, WhereOption.REPLACE), UpdateFilter.ofIncluded(false).add("tombstone"));
        }
    }

    /**
     * 根据 ID 列表恢复删除的数据
     *
     * @param ids 待恢复的数据 id 集合
     * @return 恢复删除成功的数据条数
     */
    public long revokeDelete(long... ids) {
        return this.revokeDelete(new Query().in("id", ids));
    }

    /**
     * 根据指定条件恢复删除的数据
     *
     * @param query 指定的条件
     * @return 恢复删除成功的数据条数
     */
    public long revokeDelete(Query query) {
        if (!ScxContext.easyConfig().tombstone()) {
            throw new RuntimeException("物理删除模式下不允许恢复删除!!!");
        } else {
            var needRevokeDeleteModel = ScxContext.getBean(entityClass);
            needRevokeDeleteModel.tombstone = false;
            //关于 query 字段 :  恢复删除的必要条件是 已经被删除了 也就是 tombstone 为 true 所以在此做一个特殊处理
            //关于 updateFilter : 这里已经明确 实体类的所需字段不为空 所以为了性能此处 UpdateFilter 关闭 excludeIfFieldValueIsNull 功能
            return this._update(needRevokeDeleteModel, query.equal("tombstone", true, WhereOption.REPLACE), UpdateFilter.ofIncluded(false).add("tombstone"));
        }
    }

    /**
     * 根据 ID 列表恢复删除的数据
     *
     * @param con a {@link java.sql.Connection} object
     * @param ids 待恢复的数据 id 集合
     * @return 恢复删除成功的数据条数
     * @throws SQLException s
     */
    public long revokeDelete(Connection con, long... ids) throws SQLException {
        return this.revokeDelete(con, new Query().in("id", ids));
    }

    /**
     * 根据指定条件恢复删除的数据
     *
     * @param con   a {@link java.sql.Connection} object
     * @param query 指定的条件
     * @return 恢复删除成功的数据条数
     * @throws SQLException s
     */
    public long revokeDelete(Connection con, Query query) throws SQLException {
        if (!ScxContext.easyConfig().tombstone()) {
            throw new RuntimeException("物理删除模式下不允许恢复删除!!!");
        } else {
            var needRevokeDeleteModel = ScxContext.getBean(entityClass);
            needRevokeDeleteModel.tombstone = false;
            //关于 query 字段 :  恢复删除的必要条件是 已经被删除了 也就是 tombstone 为 true 所以在此做一个特殊处理
            //关于 updateFilter : 这里已经明确 实体类的所需字段不为空 所以为了性能此处 UpdateFilter 关闭 excludeIfFieldValueIsNull 功能
            return this._update(con, needRevokeDeleteModel, query.equal("tombstone", true, WhereOption.REPLACE), UpdateFilter.ofIncluded(false).add("tombstone"));
        }
    }

}
