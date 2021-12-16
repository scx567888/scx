package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.bo.Query;
import cool.scx.dao.ScxDaoColumnInfo;
import cool.scx.dao.ScxDaoTableInfo;
import cool.scx.sql.SQLBuilder;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.handler.BeanListHandler;
import cool.scx.sql.handler.ScalarHandler;
import cool.scx.util.ObjectUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 最基本的 可以实现 实体类 CRUD 的 Service
 * 注意和 {@link BaseModelService} 进行区分
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
     * 从泛型中获取 entityClass
     */
    @SuppressWarnings("unchecked")
    public BasicService() {
        var genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            this.entityClass = (Class<Entity>) typeArguments[0];
            this.scxDaoTableInfo = new ScxDaoTableInfo(this.entityClass);
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
    }

    /**
     * 保存单条数据
     *
     * @param entity 待插入的数据
     * @param con    外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 插入成功的主键 ID 如果插入失败则返回 null
     * @throws java.sql.SQLException if any.
     */
    public final Long _insert(Connection con, Entity entity) throws SQLException {
        var parameter = _buildInsertParameter(entity);
        var updateResult = SQLRunner.update(con, parameter.sql(), parameter.param());
        return updateResult.generatedKeys().size() > 0 ? updateResult.generatedKeys().get(0) : -1;
    }

    /**
     * 保存单条数据
     *
     * @param entity 待插入的数据
     * @return 插入成功的主键 ID 如果插入失败则返回 null
     */
    public final Long _insert(Entity entity) {
        var parameter = _buildInsertParameter(entity);
        var updateResult = ScxContext.sqlRunner().update(parameter.sql(), parameter.param());
        return updateResult.generatedKeys().size() > 0 ? updateResult.generatedKeys().get(0) : -1;
    }

    /**
     * a
     *
     * @param entity a
     * @return a
     */
    private SQLRunnerParameterWrapper<Map<String, Object>> _buildInsertParameter(Entity entity) {
        var insertColumns = Stream.of(scxDaoTableInfo.canInsertColumnInfos).filter(c -> c.getFieldValue(entity) != null).toArray(ScxDaoColumnInfo[]::new);
        var sql = SQLBuilder
                .Insert(scxDaoTableInfo.tableName, insertColumns)
                .Values(insertColumns)
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, ObjectUtils.mapper().convertValue(entity, ObjectUtils.MAP_TYPE));
    }

    /**
     * 保存多条数据
     *
     * @param entityList 待保存的列表
     * @param con        外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 保存成功的主键 (ID) 列表
     * @throws java.sql.SQLException if any.
     */
    public final List<Long> _insertBatch(Connection con, List<Entity> entityList) throws SQLException {
        var parameter = _buildInsertBatchParameter(entityList);
        return SQLRunner.updateBatch(con, parameter.sql(), parameter.param()).generatedKeys();
    }

    /**
     * 保存多条数据
     *
     * @param entityList 待保存的列表
     * @return 保存成功的主键 (ID) 列表
     */
    public final List<Long> _insertBatch(List<Entity> entityList) {
        var parameter = _buildInsertBatchParameter(entityList);
        return ScxContext.sqlRunner().updateBatch(parameter.sql(), parameter.param()).generatedKeys();
    }

    /**
     * a
     *
     * @param entityList a
     * @return a
     */
    private SQLRunnerParameterWrapper<ArrayList<Map<String, Object>>> _buildInsertBatchParameter(List<Entity> entityList) {
        //将 entity 转换为 map
        var mapList = new ArrayList<Map<String, Object>>(entityList.size());
        for (var entity : entityList) {
            var map = new HashMap<String, Object>();
            for (var canInsertField : scxDaoTableInfo.canInsertColumnInfos) {
                map.put(canInsertField.fieldName(), canInsertField.getFieldValue(entity));
            }
            mapList.add(map);
        }
        var sql = SQLBuilder
                .Insert(scxDaoTableInfo.tableName, scxDaoTableInfo.canInsertColumnInfos)
                .Values(scxDaoTableInfo.canInsertColumnInfos)
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, mapList);
    }

    /**
     * 获取列表
     *
     * @param query 查询过滤条件.
     * @param con   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public final List<Entity> _select(Connection con, Query query) throws SQLException {
        var parameter = _buildSelectParameter(query);
        return SQLRunner.query(con, parameter.sql(), new BeanListHandler<>(entityClass), parameter.param());
    }

    /**
     * 获取列表
     *
     * @param query 查询过滤条件.
     * @return a {@link java.util.List} object.
     */
    public final List<Entity> _select(Query query) {
        var parameter = _buildSelectParameter(query);
        return ScxContext.sqlRunner().query(parameter.sql(), new BeanListHandler<>(entityClass), parameter.param());
    }

    /**
     * a
     *
     * @param query a
     * @return a
     */
    private SQLRunnerParameterWrapper<Map<String, Object>> _buildSelectParameter(Query query) {
        var sql = SQLBuilder
                .Select(query.selectFilter().filter(scxDaoTableInfo.allColumnInfos))
                .From(scxDaoTableInfo.tableName)
                .Where(query.where())
                .GroupBy(query.groupBy())
                .OrderBy(query.orderBy())
                .Limit(query.pagination())
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParamMap());
    }

    /**
     * 获取条数
     *
     * @param query 查询条件
     * @param con   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 条数
     * @throws java.sql.SQLException if any.
     */
    public final long _count(Connection con, Query query) throws SQLException {
        var parameter = _buildCountParameter(query);
        return SQLRunner.query(con, parameter.sql(), new ScalarHandler<>("count"), parameter.param());
    }

    /**
     * 获取条数
     *
     * @param query 查询条件
     * @return 条数
     */
    public final long _count(Query query) {
        var parameter = _buildCountParameter(query);
        return ScxContext.sqlRunner().query(parameter.sql(), new ScalarHandler<>("count"), parameter.param());
    }

    /**
     * a
     *
     * @param query a
     * @return a
     */
    private SQLRunnerParameterWrapper<Map<String, Object>> _buildCountParameter(Query query) {
        var sql = SQLBuilder
                .Select("COUNT(*) AS count")
                .From(scxDaoTableInfo.tableName)
                .Where(query.where())
                .GroupBy(query.groupBy())
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParamMap());
    }

    /**
     * 更新数据
     *
     * @param entity      要更新的数据
     * @param query       更新的过滤条件
     * @param includeNull a boolean.
     * @param con         外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 受影响的条数
     * @throws java.sql.SQLException if any.
     */
    public final long _update(Connection con, Entity entity, Query query, boolean includeNull) throws SQLException {
        var parameter = _buildUpdateParameter(entity, query, includeNull);
        return SQLRunner.update(con, parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * 更新数据
     *
     * @param entity      要更新的数据
     * @param query       更新的过滤条件
     * @param includeNull a boolean.
     * @return 受影响的条数
     */
    public final long _update(Entity entity, Query query, boolean includeNull) {
        var parameter = _buildUpdateParameter(entity, query, includeNull);
        return ScxContext.sqlRunner().update(parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * a
     *
     * @param entity      a
     * @param query       a
     * @param includeNull a
     * @return a
     */
    private SQLRunnerParameterWrapper<Map<String, Object>> _buildUpdateParameter(Entity entity, Query query, boolean includeNull) {
        if (query == null || query.where().isEmpty()) {
            throw new RuntimeException("更新数据时 必须指定 id , 删除条件 或 自定义的 where 语句 !!!");
        }
        var u = includeNull ? scxDaoTableInfo.canUpdateColumnInfos : Stream.of(scxDaoTableInfo.canUpdateColumnInfos).filter(field -> field.getFieldValue(entity) != null).toArray(ScxDaoColumnInfo[]::new);
        if (u.length == 0) {
            throw new RuntimeException("更新数据时 待更新的数据 [实体类中除被 @Column(excludeOnUpdate = true) 修饰以外的字段] 不能全部为 null !!!");
        }
        var entityMap = ObjectUtils.mapper().convertValue(entity, ObjectUtils.MAP_TYPE);
        entityMap.putAll(query.where().getWhereParamMap());
        var sql = SQLBuilder
                .Update(scxDaoTableInfo.tableName)
                .Set(u)
                .Where(query.where())
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, entityMap);
    }

    /**
     * 删除数据
     *
     * @param query      where 条件
     * @param connection 外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 受影响的条数
     * @throws java.sql.SQLException if any.
     */
    public final long _delete(Connection connection, Query query) throws SQLException {
        var parameter = _buildDeleteParameter(query);
        return SQLRunner.update(connection, parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * 删除数据
     *
     * @param query where 条件
     * @return 受影响的条数
     */
    public final long _delete(Query query) {
        var parameter = _buildDeleteParameter(query);
        return ScxContext.sqlRunner().update(parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * a
     *
     * @param query a
     * @return a
     */
    private SQLRunnerParameterWrapper<Map<String, Object>> _buildDeleteParameter(Query query) {
        if (query == null || query.where().isEmpty()) {
            throw new RuntimeException("删除数据时必须指定 id , 删除条件 或 自定义的 where 语句 !!!");
        }
        var sql = SQLBuilder
                .Delete(scxDaoTableInfo.tableName)
                .Where(query.where())
                .GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParamMap());
    }

    private record SQLRunnerParameterWrapper<T>(String sql, T param) {

    }

}
