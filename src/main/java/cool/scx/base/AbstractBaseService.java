package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.bo.Query;
import cool.scx.dao.TableInfo;
import cool.scx.sql.SQLBuilder;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.UpdateResult;
import cool.scx.sql.handler.BeanListHandler;
import cool.scx.sql.handler.ScalarHandler;
import cool.scx.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>Abstract AbstractBaseService class.</p>
 *
 * @author scx567888
 * @version 1.5.1
 */
public abstract class AbstractBaseService<Entity> {

    /**
     * 实体类对应的 table 结构
     */
    protected final TableInfo tableInfo;

    /**
     * 实体类 class 用于泛型转换
     */
    protected final Class<Entity> entityClass;

    /**
     * 从泛型中获取 entityClass
     */
    @SuppressWarnings("unchecked")
    public AbstractBaseService() {
        var genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            this.entityClass = (Class<Entity>) typeArguments[0];
            this.tableInfo = new TableInfo(this.entityClass);
        } else {
            throw new IllegalArgumentException(this.getClass().getName() + " : 必须设置泛型参数 !!!");
        }
    }

    /**
     * 手动创建 entityClass
     *
     * @param entityClass 继承自 {@link cool.scx.base.BaseModel} 的实体类 class
     */
    public AbstractBaseService(Class<Entity> entityClass) {
        this.entityClass = entityClass;
        this.tableInfo = new TableInfo(this.entityClass);
    }

    /**
     * 保存单条数据
     *
     * @param entity                待插入的数据
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 插入成功的主键 ID 如果插入失败则返回 null
     * @throws java.sql.SQLException if any.
     */
    final Long _insert(Entity entity, boolean useInternalConnection, Connection con) throws SQLException {
        var c = Stream.of(tableInfo.canInsertFields).filter(field -> ObjectUtils.getFieldValue(field, entity) != null).toArray(Field[]::new);
        var sql = SQLBuilder
                .Insert(tableInfo.tableName, c)
                .Values(c)
                .GetSQL();
        UpdateResult updateResult;
        if (useInternalConnection) {
            updateResult = ScxContext.sqlRunner().update(sql, ObjectUtils.convertValueToMap(entity));
        } else {
            updateResult = SQLRunner.update(con, sql, ObjectUtils.convertValueToMap(entity));
        }
        return updateResult.generatedKeys().size() > 0 ? updateResult.generatedKeys().get(0) : -1;
    }

    /**
     * 保存多条数据
     *
     * @param entityList            待保存的列表
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 保存成功的主键 (ID) 列表
     * @throws java.sql.SQLException if any.
     */
    final List<Long> _insertBatch(List<Entity> entityList, boolean useInternalConnection, Connection con) throws SQLException {
        //将 entity 转换为 map
        var mapList = new ArrayList<Map<String, Object>>(entityList.size());
        for (var entity : entityList) {
            var map = new HashMap<String, Object>();
            for (var canInsertField : tableInfo.canInsertFields) {
                map.put(canInsertField.getName(), ObjectUtils.getFieldValue(canInsertField, entity));
            }
            mapList.add(map);
        }
        var sql = SQLBuilder
                .Insert(tableInfo.tableName, tableInfo.canInsertFields)
                .Values(tableInfo.canInsertFields)
                .GetSQL();
        if (useInternalConnection) {
            return ScxContext.sqlRunner().updateBatch(sql, mapList).generatedKeys();
        } else {
            return SQLRunner.updateBatch(con, sql, mapList).generatedKeys();
        }
    }

    /**
     * 获取列表
     *
     * @param query                 查询过滤条件.
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    final List<Entity> _select(Query query, boolean useInternalConnection, Connection con) throws SQLException {
        var sql = SQLBuilder
                .Select(tableInfo.selectColumns)
                .From(tableInfo.tableName)
                .Where(query.where())
                .GroupBy(query.groupBy())
                .OrderBy(query.orderBy())
                .Limit(query.pagination())
                .GetSQL();
        if (useInternalConnection) {
            return ScxContext.sqlRunner().query(sql, new BeanListHandler<>(entityClass), query.where().getWhereParamMap());
        } else {
            return SQLRunner.query(con, sql, new BeanListHandler<>(entityClass), query.where().getWhereParamMap());
        }
    }

    /**
     * 获取条数
     *
     * @param query                 查询条件
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 条数
     * @throws java.sql.SQLException if any.
     */
    final long _count(Query query, boolean useInternalConnection, Connection con) throws SQLException {
        var sql = SQLBuilder
                .Select("COUNT(*) AS count")
                .From(tableInfo.tableName)
                .Where(query.where())
                .GroupBy(query.groupBy())
                .GetSQL();
        if (useInternalConnection) {
            return ScxContext.sqlRunner().query(sql, new ScalarHandler<>("count"), query.where().getWhereParamMap());
        } else {
            return SQLRunner.query(con, sql, new ScalarHandler<>("count"), query.where().getWhereParamMap());
        }
    }

    /**
     * 更新数据
     *
     * @param entity                要更新的数据
     * @param query                 更新的过滤条件
     * @param includeNull           a boolean.
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 受影响的条数
     * @throws java.sql.SQLException if any.
     */
    final long _update(Entity entity, Query query, boolean includeNull, boolean useInternalConnection, Connection con) throws SQLException {
        if (query == null || query.where().isEmpty()) {
            throw new RuntimeException("更新数据时 必须指定 id , 删除条件 或 自定义的 where 语句 !!!");
        }
        var u = includeNull ? tableInfo.canUpdateFields : Stream.of(tableInfo.canUpdateFields).filter(field -> ObjectUtils.getFieldValue(field, entity) != null).toArray(Field[]::new);
        if (u.length == 0) {
            throw new RuntimeException("更新数据时 待更新的数据 [实体类中除被 @Column(excludeOnUpdate = true) 修饰以外的字段] 不能全部为 null !!!");
        }
        var entityMap = ObjectUtils.convertValueToMap(entity);
        entityMap.putAll(query.where().getWhereParamMap());
        var sql = SQLBuilder
                .Update(tableInfo.tableName)
                .Set(u)
                .Where(query.where())
                .GetSQL();
        if (useInternalConnection) {
            return ScxContext.sqlRunner().update(sql, entityMap).affectedLength();
        } else {
            return SQLRunner.update(con, sql, entityMap).affectedLength();
        }
    }

    /**
     * 删除数据
     *
     * @param query                 where 条件
     * @param useInternalConnection 使用内部连接
     * @param con                   外部传来的连接 (useInternalConnection 为 false 是使用)
     * @return 受影响的条数
     * @throws java.sql.SQLException if any.
     */
    final long _delete(Query query, boolean useInternalConnection, Connection con) throws SQLException {
        if (query == null || query.where().isEmpty()) {
            throw new RuntimeException("删除数据时必须指定 id , 删除条件 或 自定义的 where 语句 !!!");
        }
        var sql = SQLBuilder
                .Delete(tableInfo.tableName)
                .Where(query.where())
                .GetSQL();
        if (useInternalConnection) {
            return ScxContext.sqlRunner().update(sql, query.where().getWhereParamMap()).affectedLength();
        } else {
            return SQLRunner.update(con, sql, query.where().getWhereParamMap()).affectedLength();
        }
    }

}
