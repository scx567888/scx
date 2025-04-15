package cool.scx.data;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static cool.scx.data.field_policy.FieldPolicyBuilder.ofExcluded;
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
    /// @param entity      数据
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID (无主键则为 null)
    ID add(Entity entity, FieldPolicy fieldPolicy);

    /// 添加多条数据
    ///
    /// @param entityList  数据
    /// @param fieldPolicy 字段策略
    /// @return 主键 ID 列表 (无主键则为 null)
    List<ID> add(Collection<Entity> entityList, FieldPolicy fieldPolicy);

    /// 查询多条数据
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 数据列表
    List<Entity> find(Query query, FieldPolicy fieldPolicy);

    /// 查询多条数据
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @param consumer    消费者
    void find(Query query, FieldPolicy fieldPolicy, Consumer<Entity> consumer);

    /// 查询单条数据
    /// 如果匹配到多个会返回第一个 如果无匹配会返回 null
    ///
    /// @param query       查询条件
    /// @param fieldPolicy 字段策略
    /// @return 数据
    Entity get(Query query, FieldPolicy fieldPolicy);

    /// 更新数据
    /// 当 entity 为 null 时, fieldPolicy 必须包含至少一个字段表达式
    ///
    /// @param entity      需要更新的数据 可以为 null
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

    /// 获取 类
    ///
    /// @return a
    Class<Entity> entityClass();

    default ID add(Entity entity) {
        return add(entity, ofExcluded());
    }

    default List<ID> add(Collection<Entity> entityList) {
        return add(entityList, ofExcluded());
    }

    default List<Entity> find(Query query) {
        return find(query, ofExcluded());
    }

    default List<Entity> find(FieldPolicy fieldFilter) {
        return find(query(), fieldFilter);
    }

    default List<Entity> find() {
        return find(query(), ofExcluded());
    }

    default void find(Query query, Consumer<Entity> consumer) {
        find(query, ofExcluded(), consumer);
    }

    default void find(FieldPolicy fieldFilter, Consumer<Entity> consumer) {
        find(query(), fieldFilter, consumer);
    }

    default void find(Consumer<Entity> consumer) {
        find(query(), ofExcluded(), consumer);
    }

    default Entity get(Query query) {
        return get(query, ofExcluded());
    }

    default long update(Entity entity, Query query) {
        return update(entity, query, ofExcluded());
    }

    default long update(Query query, FieldPolicy fieldPolicy) {
        return update(null, query, fieldPolicy);
    }

}
