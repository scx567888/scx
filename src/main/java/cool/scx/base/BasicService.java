package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.dao.ScxDaoTableInfo;
import cool.scx.sql.PlaceholderSQL;
import cool.scx.sql.SQLBuilder;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.handler.BeanListHandler;
import cool.scx.sql.handler.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
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
     * @return 插入成功的主键 ID 如果插入失败则返回 null
     */
    public final Long _insert(Entity entity, UpdateFilter updateFilter) {
        var parameter = _buildInsertParameter(entity, updateFilter);
        var updateResult = ScxContext.sqlRunner().update(parameter.sql(), parameter.param());
        return updateResult.generatedKeys().size() > 0 ? updateResult.generatedKeys().get(0) : -1;
    }

    /**
     * 保存单条数据
     *
     * @param entity       待插入的数据
     * @param con          外部传来的连接 (useInternalConnection 为 false 是使用)
     * @param updateFilter a
     * @return 插入成功的主键 ID 如果插入失败则返回 null
     * @throws java.sql.SQLException if any.
     */
    public final Long _insert(Connection con, Entity entity, UpdateFilter updateFilter) throws SQLException {
        var parameter = _buildInsertParameter(entity, updateFilter);
        var updateResult = SQLRunner.update(con, parameter.sql(), parameter.param());
        return updateResult.generatedKeys().size() > 0 ? updateResult.generatedKeys().get(0) : -1;
    }

    /**
     * a
     *
     * @param entity       a
     * @param updateFilter a
     * @return a
     */
    private SQLRunnerParameterWrapper<Object[]> _buildInsertParameter(Entity entity, UpdateFilter updateFilter) {
        var insertColumns = updateFilter != null ? updateFilter.filter(entity, scxDaoTableInfo.columnInfos()) : scxDaoTableInfo.columnInfos();
        //insert 允许空列所以这里不做判断
        var sql = SQLBuilder.Insert(scxDaoTableInfo.tableName(), insertColumns).Values(insertColumns).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, Arrays.stream(insertColumns).map(c -> c.getFieldValue(entity)).toArray());
    }

    /**
     * 保存多条数据
     *
     * @param entityList   待保存的列表
     * @param updateFilter a
     * @return 保存成功的主键 (ID) 列表
     */
    public final List<Long> _insertBatch(List<Entity> entityList, UpdateFilter updateFilter) {
        var parameter = _buildInsertBatchParameter(entityList, updateFilter);
        return ScxContext.sqlRunner().updateBatch(new PlaceholderSQL(parameter.sql(), parameter.param())).generatedKeys();
    }

    /**
     * 保存多条数据
     *
     * @param entityList   待保存的列表
     * @param con          外部传来的连接 (useInternalConnection 为 false 是使用)
     * @param updateFilter a
     * @return 保存成功的主键 (ID) 列表
     * @throws java.sql.SQLException if any.
     */
    public final List<Long> _insertBatch(Connection con, List<Entity> entityList, UpdateFilter updateFilter) throws SQLException {
        var parameter = _buildInsertBatchParameter(entityList, updateFilter);
        return SQLRunner.updateBatch(con, new PlaceholderSQL(parameter.sql(), parameter.param())).generatedKeys();
    }

    /**
     * a
     *
     * @param entityList   a
     * @param updateFilter a
     * @return a
     */
    private SQLRunnerParameterWrapper<List<Object[]>> _buildInsertBatchParameter(List<Entity> entityList, UpdateFilter updateFilter) {
        var insertColumns = updateFilter != null ? updateFilter.filter(scxDaoTableInfo.columnInfos()) : scxDaoTableInfo.columnInfos();
        //将 entityList 转换为 objectArrayList
        var objectArrayList = entityList.stream().map(entity -> Arrays.stream(insertColumns).map(c -> c.getFieldValue(entity)).toArray()).toList();
        var sql = SQLBuilder.Insert(scxDaoTableInfo.tableName(), insertColumns).Values(insertColumns).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, objectArrayList);
    }

    /**
     * 获取列表
     *
     * @param query        查询过滤条件.
     * @param selectFilter a
     * @return a {@link java.util.List} object.
     */
    public final List<Entity> _select(Query query, SelectFilter selectFilter) {
        var parameter = _buildSelectParameter(query, selectFilter);
        return ScxContext.sqlRunner().query(parameter.sql(), entityBeanListHandler, parameter.param());
    }

    /**
     * 获取列表
     *
     * @param query        查询过滤条件.
     * @param con          外部传来的连接 (useInternalConnection 为 false 是使用)
     * @param selectFilter a
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public final List<Entity> _select(Connection con, Query query, SelectFilter selectFilter) throws SQLException {
        var parameter = _buildSelectParameter(query, selectFilter);
        return SQLRunner.query(con, parameter.sql(), entityBeanListHandler, parameter.param());
    }

    /**
     * a
     *
     * @param query        a
     * @param selectFilter a
     * @return a
     */
    private SQLRunnerParameterWrapper<Object[]> _buildSelectParameter(Query query, SelectFilter selectFilter) {
        var selectColumnInfos = selectFilter != null ? selectFilter.filter(scxDaoTableInfo.columnInfos()) : scxDaoTableInfo.columnInfos();
        if (selectColumnInfos.length == 0) {
            throw new IllegalArgumentException("查询数据时 待查询的数据列 不能为空 !!!");
        }
        var sql = SQLBuilder.Select(selectColumnInfos).From(scxDaoTableInfo.tableName()).Where(query.where()).GroupBy(query.groupBy()).OrderBy(query.orderBy()).Limit(query.pagination()).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParams());
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
     * a
     *
     * @param query a
     * @return a
     */
    private SQLRunnerParameterWrapper<Object[]> _buildCountParameter(Query query) {
        var sql = SQLBuilder.Select("COUNT(*) AS count").From(scxDaoTableInfo.tableName()).Where(query.where()).GroupBy(query.groupBy()).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParams());
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
        var parameter = _buildUpdateParameter(entity, query, updateFilter);
        return ScxContext.sqlRunner().update(parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * 更新数据
     *
     * @param entity       要更新的数据
     * @param query        更新的过滤条件
     * @param con          外部传来的连接 (useInternalConnection 为 false 是使用)
     * @param updateFilter a
     * @return 受影响的条数
     * @throws java.sql.SQLException if any.
     */
    public final long _update(Connection con, Entity entity, Query query, UpdateFilter updateFilter) throws SQLException {
        var parameter = _buildUpdateParameter(entity, query, updateFilter);
        return SQLRunner.update(con, parameter.sql(), parameter.param()).affectedLength();
    }

    /**
     * a
     *
     * @param entity       a
     * @param query        a
     * @param updateFilter a
     * @return a
     */
    private SQLRunnerParameterWrapper<Object[]> _buildUpdateParameter(Entity entity, Query query, UpdateFilter updateFilter) {
        if (query == null || query.where().isEmpty()) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var updateSetColumnInfos = updateFilter != null ? updateFilter.filter(entity, scxDaoTableInfo.columnInfos()) : scxDaoTableInfo.columnInfos();
        if (updateSetColumnInfos.length == 0) {
            throw new IllegalArgumentException("更新数据时 待更新的数据列 不能为空 !!!");
        }
        var entityParams = Arrays.stream(updateSetColumnInfos).map(c -> c.getFieldValue(entity)).collect(Collectors.toList());
        entityParams.addAll(List.of(query.where().getWhereParams()));
        var sql = SQLBuilder.Update(scxDaoTableInfo.tableName()).Set(updateSetColumnInfos).Where(query.where()).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, entityParams.toArray());
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
     * a
     *
     * @param query a
     * @return a
     */
    private SQLRunnerParameterWrapper<Object[]> _buildDeleteParameter(Query query) {
        if (query == null || query.where().isEmpty()) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var sql = SQLBuilder.Delete(scxDaoTableInfo.tableName()).Where(query.where()).GetSQL();
        return new SQLRunnerParameterWrapper<>(sql, query.where().getWhereParams());
    }

    private record SQLRunnerParameterWrapper<T>(String sql, T param) {

    }

}
