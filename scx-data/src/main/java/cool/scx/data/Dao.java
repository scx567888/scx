package cool.scx.data;

import java.util.Collection;
import java.util.List;

import static cool.scx.data.FieldFilter.ofExcluded;

/**
 * 用于定义数据访问层的规范
 *
 * @param <Entity>
 * @param <ID>
 * @author scx567888
 * @version 0.1.3
 */
public interface Dao<Entity, ID> {

    ID add(Entity entity, FieldFilter fieldFilter);

    /**
     * 像数据源中添加一条数据
     *
     * @param entity 实体类
     * @return 主键 ID (无主键则为 null)
     */
    default ID add(Entity entity) {
        return add(entity, ofExcluded());
    }

    /**
     * 像数据源中添加多条数据
     *
     * @param entityList 实体类
     * @return 主键 ID 列表 (无主键则为 null)
     */
    List<ID> addAll(Collection<Entity> entityList, FieldFilter fieldFilter);

    /**
     * 像数据源中添加多条数据
     *
     * @param entityList 实体类
     * @return 主键 ID 列表 (无主键则为 null)
     */
    default List<ID> addAll(Collection<Entity> entityList) {
        return addAll(entityList, ofExcluded());
    }

    /**
     * 查询多条数据
     *
     * @param query 查询条件
     * @return 数据列表
     */
    List<Entity> find(ReadableQuery query, FieldFilter fieldFilter);

    default List<Entity> find(ReadableQuery query) {
        return find(query, ofExcluded());
    }

    default List<Entity> find(FieldFilter fieldFilter) {
        return find(new Query(), fieldFilter);
    }

    default List<Entity> find() {
        return find(new Query(), ofExcluded());
    }

    /**
     * 查询单条数据
     *
     * @param query 查询条件
     * @return 数据列表
     */
    Entity get(ReadableQuery query, FieldFilter fieldFilter);

    default Entity get(ReadableQuery query) {
        return get(query, ofExcluded());
    }

    /**
     * 更新数据
     *
     * @param entity 需要更新的数据
     * @param query  查询条件
     * @return 更新成功的条数
     */
    long update(Entity entity, ReadableQuery query, FieldFilter fieldFilter);

    /**
     * 更新数据
     *
     * @param entity 需要更新的数据
     * @param query  查询条件
     * @return 更新成功的条数
     */
    default long update(Entity entity, ReadableQuery query) {
        return update(entity, query, ofExcluded());
    }

    /**
     * 删除
     *
     * @param query 查询条件
     * @return 删除成功的条数
     */
    long delete(ReadableQuery query);

    /**
     * 查询行数
     *
     * @param query 查询条件
     * @return 符合条件的行数
     */
    long count(ReadableQuery query);

    /**
     * 清空整个数据源 (慎用)
     */
    void _clear();

    /**
     * 获取 类
     *
     * @return a
     */
    Class<Entity> _entityClass();

}
