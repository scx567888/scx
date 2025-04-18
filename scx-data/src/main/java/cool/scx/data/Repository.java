package cool.scx.data;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includedAll;
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
    /// @param entity      待插入的数据 (可以为 null)
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID (若数据没有主键, 则为 null)
    ID add(Entity entity, FieldPolicy fieldPolicy);

    /// 添加多条数据
    ///
    /// 因为无法判断 entityList 中每个成员的情况, 所以所有插入字段均由 fieldPolicy 提供
    ///
    /// @param entityList  待插入的数据列表 (成员 可以为 null)
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID 列表 (若数据没有主键, 则为 null 列表)
    List<ID> add(Collection<Entity> entityList, FieldPolicy fieldPolicy);

    /// 查询多条数据
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 数据列表
    List<Entity> find(Query query, FieldPolicy fieldPolicy);

    /// 查询多条数据
    ///
    /// @param query          查询条件
    /// @param fieldPolicy    字段策略
    /// @param entityConsumer 数据消费者
    void find(Query query, FieldPolicy fieldPolicy, Consumer<Entity> entityConsumer);

    /// 查询单条数据
    ///
    /// - 如果匹配到多条数据, 则返回第一个匹配项
    /// - 如果没有匹配项, 返回 null
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 数据
    Entity get(Query query, FieldPolicy fieldPolicy);

    /// 更新数据
    ///
    /// 当 entity 为 null 时, 将使用 fieldPolicy 进行纯表达式更新, 此时要求 fieldPolicy 必须包含至少一个字段表达式
    ///
    /// @param entity      需要更新的数据 (可以为 null)
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 更新成功的条数
    long update(Entity entity, Query query, FieldPolicy fieldPolicy);

    /// 删除数据
    ///
    /// @param query 查询条件
    /// @return 删除成功的条数
    long delete(Query query);

    /// 查询行数
    ///
    /// @param query 查询条件
    /// @return 符合条件的行数
    long count(Query query);

    /// 清空整个数据源 (慎用)
    void clear();

    default ID add(Entity entity) {
        return add(entity, includedAll());
    }

    default ID add(FieldPolicy fieldPolicy) {
        return add((Entity) null, fieldPolicy);
    }

    default List<ID> add(Collection<Entity> entityList) {
        return add(entityList, includedAll());
    }

    default List<Entity> find(Query query) {
        return find(query, includedAll());
    }

    default List<Entity> find(FieldPolicy fieldFilter) {
        return find(query(), fieldFilter);
    }

    default List<Entity> find() {
        return find(query(), includedAll());
    }

    default void find(Query query, Consumer<Entity> entityConsumer) {
        find(query, includedAll(), entityConsumer);
    }

    default void find(FieldPolicy fieldFilter, Consumer<Entity> entityConsumer) {
        find(query(), fieldFilter, entityConsumer);
    }

    default void find(Consumer<Entity> entityConsumer) {
        find(query(), includedAll(), entityConsumer);
    }

    default Entity get(Query query) {
        return get(query, includedAll());
    }

    default long update(Entity entity, Query query) {
        return update(entity, query, includedAll());
    }

    default long update(Query query, FieldPolicy fieldPolicy) {
        return update(null, query, fieldPolicy);
    }

    default long count() {
        return count(query());
    }

}
