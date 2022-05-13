package cool.scx.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static cool.scx.base.BaseModelServiceHelper.queryProcessor;

/**
 * 增强版的 baseModelService 提供了 添加并获取 和 更新并获取 方法
 *
 * @param <Entity> e
 */
public class EnhancedBaseModelService<Entity extends BaseModel> extends BaseModelService<Entity> {

    public EnhancedBaseModelService() {
        super();
    }

    public EnhancedBaseModelService(Class<Entity> entityClass) {
        super(entityClass);
    }

    /**
     * 插入数据
     *
     * @param entity 待插入的数据
     * @return 插入后的数据
     */
    public final Entity saveAndGet(Entity entity) {
        var newID = this.save(entity);
        return newID != null ? this.get(newID) : null;
    }

    /**
     * a
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    public final Entity saveAndGet(Entity entity, UpdateFilter updateFilter) {
        var newID = this.save(entity, updateFilter);
        return newID != null ? this.get(newID) : null;
    }

    /**
     * 批量插入数据
     *
     * @param entityList 数据集合
     * @return 插入成功的数据的自增主键列表
     */
    public final List<Entity> saveAndGet(Collection<Entity> entityList) {
        var newIDs = this.save(entityList);
        return list(new Query().in("id", newIDs));
    }

    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    public final List<Entity> saveAndGet(Collection<Entity> entityList, UpdateFilter updateFilter) {
        var newIDs = this.save(entityList, updateFilter);
        return list(new Query().in("id", newIDs));
    }

    /**
     * 插入数据 (不使用自动提交)
     *
     * @param con    a {@link Connection} object
     * @param entity 待插入的数据
     * @return 插入后的数据
     * @throws java.sql.SQLException if any.
     */
    public final Entity saveAndGet(Connection con, Entity entity) throws SQLException {
        var newID = this.save(con, entity);
        return newID != null ? this.get(newID) : null;
    }

    /**
     * 插入数据 (不使用自动提交)
     *
     * @param con          a {@link Connection} object
     * @param entity       待插入的数据
     * @param updateFilter u
     * @return 插入后的数据
     * @throws java.sql.SQLException if any.
     */
    public final Entity saveAndGet(Connection con, Entity entity, UpdateFilter updateFilter) throws SQLException {
        var newID = this.save(con, entity, updateFilter);
        return newID != null ? this.get(newID) : null;
    }

    /**
     * 批量插入数据 (不使用自动提交)
     *
     * @param entityList 数据集合
     * @param con        a {@link java.sql.Connection} object
     * @return 插入成功的数据的自增主键列表
     * @throws java.sql.SQLException if any.
     */
    public final List<Entity> saveAndGet(Connection con, Collection<Entity> entityList) throws SQLException {
        var newIDs = this.save(con, entityList);
        return list(new Query().in("id", newIDs));
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
    public final List<Entity> saveAndGet(Connection con, Collection<Entity> entityList, UpdateFilter updateFilter) throws SQLException {
        var newIDs = this.save(con, entityList, updateFilter);
        return list(new Query().in("id", newIDs));
    }


    /**
     * 根据  id 更新
     *
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     */
    public final Entity updateAndGet(Entity entity) {
        this.update(entity);
        return this.get(entity.id);
    }

    /**
     * a
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    public final Entity updateAndGet(Entity entity, UpdateFilter updateFilter) {
        this.update(entity, updateFilter);
        return this.get(entity.id);
    }

    /**
     * 根据指定条件更新数据
     *
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     */
    public final List<Entity> updateAndGet(Entity entity, Query query) {
        queryProcessor(query);
        //因为 id 是不允许更新的所以这里可以放心获取
        var ids = this.list(query, SelectFilter.ofIncluded().addIncluded("id")).stream().map(c -> c.id).toArray();
        this.update(entity, query);
        //重新使用 id 查询数据
        return list(new Query().in("id", ids));
    }

    /**
     * a
     *
     * @param entity       a
     * @param query        a
     * @param updateFilter a
     * @return 受影响的条数
     */
    public final List<Entity> updateAndGet(Entity entity, Query query, UpdateFilter updateFilter) {
        queryProcessor(query);
        //因为 id 是不允许更新的所以这里可以放心获取
        var ids = this.list(query, SelectFilter.ofIncluded().addIncluded("id")).stream().map(c -> c.id).toArray();
        this.update(entity, query, updateFilter);
        //重新使用 id 查询数据
        return list(new Query().in("id", ids));
    }

    /**
     * 根据  id 更新
     *
     * @param con    a {@link Connection} object
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     * @throws SQLException s
     */
    public final Entity updateAndGet(Connection con, Entity entity) throws SQLException {
        this.update(con, entity);
        return this.get(con, entity.id);
    }

    /**
     * a
     *
     * @param con          a {@link Connection} object
     * @param entity       a
     * @param updateFilter a
     * @return 受影响的条数
     * @throws SQLException a
     */
    public final Entity updateAndGet(Connection con, Entity entity, UpdateFilter updateFilter) throws SQLException {
        this.update(con, entity, updateFilter);
        return this.get(con, entity.id);
    }

    /**
     * 根据指定条件更新数据
     *
     * @param con    a {@link Connection} object
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     * @throws SQLException s
     */
    public final List<Entity> updateAndGet(Connection con, Entity entity, Query query) throws SQLException {
        queryProcessor(query);
        //因为 id 是不允许更新的所以这里可以放心获取
        var ids = this.list(con, query, SelectFilter.ofIncluded().addIncluded("id")).stream().map(c -> c.id).toArray();
        this.update(con, entity, query);
        //重新使用 id 查询数据
        return list(con, new Query().in("id", ids));
    }

    /**
     * a
     *
     * @param con          a {@link Connection} object
     * @param entity       a
     * @param query        a
     * @param updateFilter a
     * @return a
     * @throws SQLException s
     */
    public final List<Entity> updateAndGet(Connection con, Entity entity, Query query, UpdateFilter updateFilter) throws SQLException {
        queryProcessor(query);
        //因为 id 是不允许更新的所以这里可以放心获取
        var ids = this.list(con, query, SelectFilter.ofIncluded().addIncluded("id")).stream().map(c -> c.id).toArray();
        this.update(con, entity, query, updateFilter);
        //重新使用 id 查询数据
        return list(con, new Query().in("id", ids));
    }

}
