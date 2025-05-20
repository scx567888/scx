package cool.scx.data;

import cool.scx.data.field_policy.QueryFieldPolicy;
import cool.scx.data.field_policy.UpdateFieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;

import static cool.scx.data.field_policy.FieldPolicyBuilder.queryIncludeAll;
import static cool.scx.data.field_policy.FieldPolicyBuilder.updateIncludeAll;
import static cool.scx.data.query.QueryBuilder.query;

/// 用于定义数据访问层的规范
///
/// @param <Entity>
/// @param <ID>
/// @author scx567888
/// @version 0.0.1
public interface Repository<Entity, ID> {

    /// 添加一条数据
    ///
    /// 当 entity 为 null 时, 将使用 fieldPolicy 进行纯表达式插入
    ///
    /// @param entity            待插入的数据 (可以为 null)
    /// @param updateFieldPolicy 字段策略
    /// @return 主键 ID (若数据没有主键, 则为 null)
    ID add(Entity entity, UpdateFieldPolicy updateFieldPolicy);

    /// 添加多条数据
    ///
    /// 因为无法判断 entityList 中每个成员的情况, 所以所有插入字段均由 fieldPolicy 提供
    ///
    /// @param entityList        待插入的数据列表 (成员 可以为 null)
    /// @param updateFieldPolicy 字段策略
    /// @return 主键 ID 列表 (若数据没有主键, 则为 null 列表)
    List<ID> add(Collection<Entity> entityList, UpdateFieldPolicy updateFieldPolicy);

    /// 创建一个数据查询器
    ///
    /// 因为查询操作复杂度较高, 进而独立出一个 Finder 概念, 但你仍可以使用 find 来覆盖大部分场景
    ///
    /// @param query            查询条件
    /// @param queryFieldPolicy 字段策略
    /// @return 查询器
    Finder<Entity> finder(Query query, QueryFieldPolicy queryFieldPolicy);

    /// 更新数据
    ///
    /// 当 entity 为 null 时, 将使用 fieldPolicy 进行纯表达式更新, 此时要求 fieldPolicy 必须包含至少一个字段表达式
    ///
    /// @param entity            需要更新的数据 (可以为 null)
    /// @param query             查询条件
    /// @param updateFieldPolicy 字段策略
    /// @return 更新成功的条数
    long update(Entity entity, UpdateFieldPolicy updateFieldPolicy, Query query);

    /// 删除数据
    ///
    /// @param query 查询条件
    /// @return 删除成功的条数
    long delete(Query query);

    /// 清空整个数据源 (慎用)
    void clear();

    default ID add(Entity entity) {
        return add(entity, updateIncludeAll());
    }

    default ID add(UpdateFieldPolicy updateFieldPolicy) {
        return add((Entity) null, updateFieldPolicy);
    }

    default List<ID> add(Collection<Entity> entityList) {
        return add(entityList, updateIncludeAll());
    }

    default Finder<Entity> finder(Query query) {
        return finder(query, queryIncludeAll());
    }

    default Finder<Entity> finder(QueryFieldPolicy queryFieldPolicy) {
        return finder(query(), queryFieldPolicy);
    }

    default Finder<Entity> finder() {
        return finder(query(), queryIncludeAll());
    }

    default List<Entity> find(Query query, QueryFieldPolicy queryFieldPolicy) {
        return finder(query, queryFieldPolicy).list();
    }

    default List<Entity> find(Query query) {
        return finder(query).list();
    }

    default List<Entity> find(QueryFieldPolicy queryFieldPolicy) {
        return finder(queryFieldPolicy).list();
    }

    default List<Entity> find() {
        return finder().list();
    }

    default Entity get(Query query, QueryFieldPolicy queryFieldPolicy) {
        return finder(query, queryFieldPolicy).first();
    }

    default Entity get(Query query) {
        return finder(query).first();
    }

    default long update(Entity entity, Query query) {
        return update(entity, updateIncludeAll(), query);
    }

    default long update(UpdateFieldPolicy updateFieldPolicy, Query query) {
        return update(null, updateFieldPolicy, query);
    }

    default long count(Query query) {
        return finder(query).count();
    }

    default long count() {
        return finder().count();
    }

}
