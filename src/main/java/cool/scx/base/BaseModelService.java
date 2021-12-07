package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.bo.Query;
import cool.scx.sql.SQLBuilder;
import cool.scx.sql.handler.MapListHandler;
import cool.scx.sql.where.Where;
import cool.scx.sql.where.WhereOption;
import cool.scx.util.CaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * 插入数据
     *
     * @param entity 待插入的数据
     * @return 插入后的数据
     */
    public Entity save(Entity entity) {
        var newId = this._insert(entity);
        return this.get(newId);
    }

    /**
     * 批量插入数据
     *
     * @param entityList 数据集合
     * @return 插入成功的数据的自增主键列表
     */
    public List<Long> save(List<Entity> entityList) {
        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<>();
        } else {
            return this._insertBatch(entityList);
        }
    }

    /**
     * 根据 ID 列表删除指定的数据
     *
     * @param ids 要删除的数据的 id 集合
     * @return 删除成功的数据条数
     */
    public long delete(long... ids) {
        //物理删除
        if (!ScxContext.easyConfig().tombstone()) {
            return this._delete(new Query().in("id", ids));
        } else {// 逻辑删除
            var needTombstoneEntity = ScxContext.beanFactory().getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            var query = new Query().in("id", ids).equal("tombstone", false);
            return this._update(needTombstoneEntity, query, false);
        }
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
            var needTombstoneEntity = ScxContext.beanFactory().getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            return this._update(needTombstoneEntity, query, false);
        }
    }

    /**
     * 根据 ID 列表删除指定的数据  (注意 : 此方法会忽略配置文件强制使用物理删除) (自动提交)
     *
     * @param ids 要删除的数据的 id 集合
     * @return 删除成功的数据条数
     */
    public long deleteIgnoreTombstone(long... ids) {
        return this._delete(new Query().in("id", ids));
    }

    /**
     * 根据条件删除指定的数据  (注意 : 此方法会忽略配置文件强制使用物理删除)  (自动提交)
     *
     * @param query 删除条件
     * @return 被删除的数据条数
     */
    public long deleteIgnoreTombstone(Query query) {
        return this._delete(query);
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
            var needRevokeDeleteModel = ScxContext.beanFactory().getBean(entityClass);
            needRevokeDeleteModel.tombstone = false;
            return this._update(needRevokeDeleteModel, query, false);
        }
    }

    /**
     * 根据指定条件更新数据
     *
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     */
    public long update(Entity entity, Query query) {
        //逻辑删除时不更新 处于逻辑删除状态的数据
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        //更新成功的条数
        return this._update(entity, query, false);
    }

    /**
     * 根据  id 更新
     *
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     */
    public Entity update(Entity entity) {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.update(entity, new Query().equal("id", entity.id));
        return l == 1 ? this.get(entity.id) : null;
    }

    /**
     * 根据指定条件更新数据 (注意 : 数据中的 null 值会被同步设置到数据库中)
     *
     * @param entity 待更新的数据
     * @param query  更新的条件
     * @return 更新成功的数据条数
     */
    public long updateIncludeNull(Entity entity, Query query) {
        //逻辑删除时不更新 处于逻辑删除状态的数据
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        //更新成功的条数
        return this._update(entity, query, true);
    }

    /**
     * 根据  id 更新  (注意 : 数据中的 null 值会被同步设置到数据库中)
     *
     * @param entity 待更新的数据 ( 注意: 请保证数据中 id 字段不为空 )
     * @return 更新成功后的数据
     */
    public Entity updateIncludeNull(Entity entity) {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.updateIncludeNull(entity, new Query().equal("id", entity.id));
        return l == 1 ? this.get(entity.id) : null;
    }

    /**
     * 根据 ID (主键) 查询单条数据
     *
     * @param id id ( 主键 )
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public Entity get(long id) {
        return get(new Query().equal("id", id));
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取单条数据
     *
     * @param query 聚合查询参数对象
     * @return 查到多个则返回第一个 没有则返回 null
     */
    public Entity get(Query query) {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        query.setPagination(1);
        var list = this._select(query);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 根据聚合查询条件 {@link Query} 获取数据条数
     *
     * @param query 聚合查询参数对象
     * @return 数据条数
     */
    public long count(Query query) {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._count(query);
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
     * 根据聚合查询条件 {@link Query} 获取数据列表
     *
     * @param query 聚合查询参数对象
     * @return 数据列表
     */
    public List<Entity> list(Query query) {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._select(query);
    }

    /**
     * 根据 ID (主键) 列表 获取数据列表
     *
     * @param ids ID (主键) 列表
     * @return 数据列表
     */
    public List<Entity> list(long... ids) {
        return list(new Query().in("id", ids));
    }

    /**
     * 获取所有数据 (注意 : 默认根据最后更新时间 {@link cool.scx.base.BaseModel#updateDate} 排序)
     *
     * @return 所有数据
     */
    public List<Entity> list() {
        var query = new Query().desc("updateDate");
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._select(query);
    }

    /**
     * 根据 fieldName 获取 list 集合 一般做 Autocomplete 用
     *
     * @param fieldName 字段名称
     * @return 以 value 为键值的 list 集合
     */
    public List<Map<String, Object>> getFieldList(String fieldName) {
        //判断查询字段是否安全 ( 数据库字段内 防止 sql 注入)
        var isSafe = Arrays.stream(this.scxDaoTableInfo.allFields)
                .filter(field -> field.getName().equals(fieldName))
                .count() == 1;
        if (isSafe) {
            var selectColumn = CaseUtils.toSnake(fieldName) + " As value ";
            var where = new Where();
            if (ScxContext.easyConfig().tombstone()) {
                where.equal("tombstone", false);
            }
            var sql = SQLBuilder
                    .Select(selectColumn)
                    .From(this.scxDaoTableInfo.tableName)
                    .Where(where)
                    .GroupBy("value")
                    .GetSQL();
            return ScxContext.sqlRunner().query(sql, new MapListHandler(), where.getWhereParamMap());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 插入数据 (不使用自动提交)
     *
     * @param entity 待插入的数据
     * @param con    a Connection object
     * @return 插入后的数据
     * @throws java.sql.SQLException if any.
     */
    public Entity save(Connection con, Entity entity) throws SQLException {
        var newId = this._insert(con, entity);
        return this.get(con, newId);
    }

    /**
     * 批量插入数据 (不使用自动提交)
     *
     * @param entityList 数据集合
     * @param con        a Connection object
     * @return 插入成功的数据的自增主键列表
     * @throws java.sql.SQLException if any.
     */
    public List<Long> save(Connection con, List<Entity> entityList) throws SQLException {
        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<>();
        } else {
            return this._insertBatch(con, entityList);
        }
    }

    /**
     * (不使用自动提交)
     *
     * @param con c
     * @param ids i
     * @return r
     * @throws java.sql.SQLException r
     */
    public long delete(Connection con, long... ids) throws SQLException {
        //物理删除
        if (!ScxContext.easyConfig().tombstone()) {
            return this._delete(con, new Query().in("id", ids));
        } else {// 逻辑删除
            var needTombstoneEntity = ScxContext.beanFactory().getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            var query = new Query().in("id", ids).equal("tombstone", false);
            return this._update(con, needTombstoneEntity, query, false);
        }
    }

    /**
     * c
     *
     * @param con   c
     * @param query c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long delete(Connection con, Query query) throws SQLException {
        //物理删除
        if (!ScxContext.easyConfig().tombstone()) {
            return this._delete(con, query);
        } else {//逻辑删除
            var needTombstoneEntity = ScxContext.beanFactory().getBean(entityClass);
            needTombstoneEntity.tombstone = true;
            return this._update(con, needTombstoneEntity, query, false);
        }
    }

    /**
     * c
     *
     * @param con c
     * @param ids c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long deleteIgnoreTombstone(Connection con, long... ids) throws SQLException {
        return this._delete(con, new Query().in("id", ids));
    }

    /**
     * c
     *
     * @param con   c
     * @param query c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long deleteIgnoreTombstone(Connection con, Query query) throws SQLException {
        return this._delete(con, query);
    }

    /**
     * c
     *
     * @param con c
     * @param ids c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long revokeDelete(Connection con, long... ids) throws SQLException {
        return this.revokeDelete(con, new Query().in("id", ids));
    }

    /**
     * c
     *
     * @param con   c
     * @param query c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long revokeDelete(Connection con, Query query) throws SQLException {
        if (!ScxContext.easyConfig().tombstone()) {
            throw new RuntimeException("物理删除模式下不允许恢复删除!!!");
        } else {
            var needRevokeDeleteModel = ScxContext.beanFactory().getBean(entityClass);
            needRevokeDeleteModel.tombstone = false;
            return this._update(con, needRevokeDeleteModel, query, false);
        }
    }

    /**
     * c
     *
     * @param con    c
     * @param entity c
     * @param query  c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long update(Connection con, Entity entity, Query query) throws SQLException {
        //逻辑删除时不更新 处于逻辑删除状态的数据
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        //更新成功的条数
        return this._update(con, entity, query, false);
    }

    /**
     * c
     *
     * @param con    c
     * @param entity c
     * @return c
     * @throws java.sql.SQLException c
     */
    public Entity update(Connection con, Entity entity) throws SQLException {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.update(con, entity, new Query().equal("id", entity.id));
        return l == 1 ? this.get(con, entity.id) : null;
    }

    /**
     * c
     *
     * @param con    c
     * @param entity c
     * @param query  c
     * @return c
     * @throws java.sql.SQLException c
     */
    public long updateIncludeNull(Connection con, Entity entity, Query query) throws SQLException {
        //逻辑删除时不更新 处于逻辑删除状态的数据
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        //更新成功的条数
        return this._update(con, entity, query, true);
    }

    /**
     * c
     *
     * @param con    c
     * @param entity c
     * @return c
     * @throws java.sql.SQLException c
     */
    public Entity updateIncludeNull(Connection con, Entity entity) throws SQLException {
        if (entity.id == null) {
            throw new RuntimeException("根据 id 更新时 id 不能为空");
        }
        var l = this.updateIncludeNull(con, entity, new Query().equal("id", entity.id));
        return l == 1 ? this.get(con, entity.id) : null;
    }

    /**
     * a
     *
     * @param con   a
     * @param query a
     * @return a
     * @throws SQLException a
     */
    public Entity get(Connection con, Query query) throws SQLException {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        query.setPagination(1);
        var list = this._select(con, query);
        return list.size() > 0 ? list.get(0) : null;
    }


    /**
     * @param con a
     * @param id  a
     * @return a
     * @throws SQLException a
     */
    public Entity get(Connection con, long id) throws SQLException {
        return get(con, new Query().equal("id", id));
    }


    /**
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con) throws SQLException {
        var query = new Query().desc("updateDate");
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._select(con, query);
    }


    /**
     * a
     *
     * @param con a
     * @param ids a
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con, long... ids) throws SQLException {
        return list(con, new Query().in("id", ids));
    }

    /**
     * a
     *
     * @param con   a
     * @param query a
     * @return a
     * @throws SQLException a
     */
    public List<Entity> list(Connection con, Query query) throws SQLException {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._select(con, query);
    }

    /**
     * a
     *
     * @param con   a
     * @param query a
     * @return a
     * @throws SQLException a
     */
    public long count(Connection con, Query query) throws SQLException {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return this._count(con, query);
    }

    /**
     * a
     *
     * @param con a
     * @return a
     * @throws SQLException a
     */
    public long count(Connection con) throws SQLException {
        return count(con, new Query());
    }

}
