package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includeAll;
import static cool.scx.data.query.QueryBuilder.query;

/// 用于定义数据访问层的规范
///
/// @param <Entity> Entity
/// @param <ID>     ID
/// @author scx567888
/// @version 0.0.1
public interface Repository<Entity, ID, C extends DataContext> {

    /// 添加一条数据
    ///
    /// 当 entity 为 null 时, 将使用 fieldPolicy 进行纯表达式插入
    ///
    /// @param entity      待插入的数据 (可以为 null)
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID (若数据没有主键, 则为 null)
    ID add(Entity entity, FieldPolicy fieldPolicy) throws DataAccessException;

    /// 支持自定义数据源的版本
    ID add(Entity entity, FieldPolicy fieldPolicy, C dataContext) throws DataAccessException;

    /// 添加多条数据
    ///
    /// 因为无法判断 entityList 中每个成员的情况, 所以所有插入字段均由 fieldPolicy 提供
    ///
    /// @param entityList  待插入的数据列表 (成员 可以为 null)
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID 列表 (若数据没有主键, 则为 null 列表)
    List<ID> add(Collection<Entity> entityList, FieldPolicy fieldPolicy) throws DataAccessException;

    /// 支持自定义数据源的版本
    List<ID> add(Collection<Entity> entityList, FieldPolicy fieldPolicy, C dataContext) throws DataAccessException;

    /// 创建一个数据查询器
    ///
    /// 因为查询操作复杂度较高, 进而独立出一个 Finder 概念, 但你仍可以使用 find 来覆盖大部分场景
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 查询器
    Finder<Entity> finder(Query query, FieldPolicy fieldPolicy);

    /// 支持自定义数据源的版本
    Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, C dataContext);

    /// 更新数据
    ///
    /// 当 entity 为 null 时, 将使用 fieldPolicy 进行纯表达式更新, 此时要求 fieldPolicy 必须包含至少一个字段表达式
    ///
    /// @param entity      需要更新的数据 (可以为 null)
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 更新成功的条数
    long update(Entity entity, FieldPolicy fieldPolicy, Query query) throws DataAccessException;

    /// 支持自定义数据源的版本
    long update(Entity entity, FieldPolicy fieldPolicy, Query query, C dataContext) throws DataAccessException;

    /// 删除数据
    ///
    /// @param query 查询条件
    /// @return 删除成功的条数
    long delete(Query query) throws DataAccessException;

    /// 支持自定义数据源的版本
    long delete(Query query, C dataContext) throws DataAccessException;

    /// 清空整个数据源 (慎用)
    void clear() throws DataAccessException;

    /// 支持自定义数据源的版本
    void clear(C dataContext) throws DataAccessException;

    default ID add(Entity entity) throws DataAccessException {
        return add(entity, includeAll());
    }

    default ID add(Entity entity, C dataContext) throws DataAccessException {
        return add(entity, includeAll(), dataContext);
    }

    default ID add(FieldPolicy fieldPolicy) throws DataAccessException {
        return add((Entity) null, fieldPolicy);
    }

    default ID add(FieldPolicy fieldPolicy, C dataContext) throws DataAccessException {
        return add((Entity) null, fieldPolicy, dataContext);
    }

    default List<ID> add(Collection<Entity> entityList) throws DataAccessException {
        return add(entityList, includeAll());
    }

    default List<ID> add(Collection<Entity> entityList, C dataContext) throws DataAccessException {
        return add(entityList, includeAll(), dataContext);
    }

    default Finder<Entity> finder(Query query) {
        return finder(query, includeAll());
    }

    default Finder<Entity> finder(Query query, C dataContext) {
        return finder(query, includeAll(), dataContext);
    }

    default Finder<Entity> finder(FieldPolicy fieldPolicy) {
        return finder(query(), fieldPolicy);
    }

    default Finder<Entity> finder(FieldPolicy fieldPolicy, C dataContext) {
        return finder(query(), fieldPolicy, dataContext);
    }

    default Finder<Entity> finder() {
        return finder(query(), includeAll());
    }

    default Finder<Entity> finder(C dataContext) {
        return finder(query(), includeAll(), dataContext);
    }

    default List<Entity> find(Query query, FieldPolicy fieldPolicy) throws DataAccessException {
        return finder(query, fieldPolicy).list();
    }

    default List<Entity> find(Query query, FieldPolicy fieldPolicy, C dataContext) throws DataAccessException {
        return finder(query, fieldPolicy, dataContext).list();
    }

    default List<Entity> find(Query query) throws DataAccessException {
        return finder(query).list();
    }

    default List<Entity> find(Query query, C dataContext) throws DataAccessException {
        return finder(query, dataContext).list();
    }

    default List<Entity> find(FieldPolicy fieldPolicy) throws DataAccessException {
        return finder(fieldPolicy).list();
    }

    default List<Entity> find(FieldPolicy fieldPolicy, C dataContext) throws DataAccessException {
        return finder(fieldPolicy, dataContext).list();
    }

    default List<Entity> find() throws DataAccessException {
        return finder().list();
    }

    default List<Entity> find(C dataContext) throws DataAccessException {
        return finder(dataContext).list();
    }

    default Entity findFirst(Query query, FieldPolicy fieldPolicy) throws DataAccessException {
        return finder(query, fieldPolicy).first();
    }

    default Entity findFirst(Query query, FieldPolicy fieldPolicy, C dataContext) throws DataAccessException {
        return finder(query, fieldPolicy, dataContext).first();
    }

    default Entity findFirst(Query query) throws DataAccessException {
        return finder(query).first();
    }

    default Entity findFirst(Query query, C dataContext) throws DataAccessException {
        return finder(query, dataContext).first();
    }

    default long update(Entity entity, Query query) throws DataAccessException {
        return update(entity, includeAll(), query);
    }

    default long update(Entity entity, Query query, C dataContext) throws DataAccessException {
        return update(entity, includeAll(), query, dataContext);
    }

    default long update(FieldPolicy fieldPolicy, Query query) throws DataAccessException {
        return update(null, fieldPolicy, query);
    }

    default long update(FieldPolicy fieldPolicy, Query query, C dataContext) throws DataAccessException {
        return update(null, fieldPolicy, query, dataContext);
    }

    default long count(Query query) throws DataAccessException {
        return finder(query).count();
    }

    default long count(Query query, C dataContext) throws DataAccessException {
        return finder(query, dataContext).count();
    }

    default long count() throws DataAccessException {
        return finder().count();
    }

    default long count(C dataContext) throws DataAccessException {
        return finder(dataContext).count();
    }

}
