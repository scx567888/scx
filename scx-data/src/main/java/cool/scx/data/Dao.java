package cool.scx.data;

import cool.scx.common.field_filter.FieldFilter;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static cool.scx.common.field_filter.FieldFilterBuilder.ofExcluded;
import static cool.scx.data.QueryBuilder.query;

/**
 * 用于定义数据访问层的规范
 *
 * @param <Entity>
 * @param <ID>
 * @author scx567888
 * @version 0.1.3
 */
public interface Dao<Entity, ID> {

    /**
     * 添加一条数据
     *
     * @param entity      数据
     * @param fieldFilter 列过滤器
     * @return 主键 ID (无主键则为 null)
     */
    ID add(Entity entity, FieldFilter fieldFilter);

    /**
     * 添加多条数据
     *
     * @param entityList  数据
     * @param fieldFilter 列过滤器
     * @return 主键 ID 列表 (无主键则为 null)
     */
    List<ID> add(Collection<Entity> entityList, FieldFilter fieldFilter);

    /**
     * 查询多条数据
     *
     * @param query       查询条件
     * @param fieldFilter 列过滤器
     * @return 数据列表
     */
    List<Entity> find(Query query, FieldFilter fieldFilter);

    /**
     * 查询多条数据
     *
     * @param query       查询条件
     * @param fieldFilter 列过滤器
     * @param consumer    消费者
     */
    void find(Query query, FieldFilter fieldFilter, Consumer<Entity> consumer);

    /**
     * 查询单条数据
     *
     * @param query       查询条件
     * @param fieldFilter 列过滤器
     * @return 数据
     */
    Entity get(Query query, FieldFilter fieldFilter);

    /**
     * 更新数据
     *
     * @param entity      需要更新的数据
     * @param query       查询条件
     * @param fieldFilter 列过滤器
     * @return 更新成功的条数
     */
    long update(Entity entity, Query query, FieldFilter fieldFilter);

    /**
     * 删除数据
     *
     * @param query 查询条件
     * @return 删除成功的条数
     */
    long delete(Query query);

    /**
     * 查询行数
     *
     * @param query 查询条件
     * @return 符合条件的行数
     */
    long count(Query query);

    /**
     * 清空整个数据源 (慎用)
     */
    void clear();

    /**
     * 获取 类
     *
     * @return a
     */
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

    default List<Entity> find(FieldFilter fieldFilter) {
        return find(query(), fieldFilter);
    }

    default List<Entity> find() {
        return find(query(), ofExcluded());
    }

    default void find(Query query, Consumer<Entity> consumer) {
        find(query, ofExcluded(), consumer);
    }

    default void find(FieldFilter fieldFilter, Consumer<Entity> consumer) {
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

}
