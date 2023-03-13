package cool.scx.dao;

import java.util.Collection;
import java.util.List;

/**
 * 最基本的 可以实现 实体类 CRUD 的 DAO
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface BaseDao<Entity, ID> {

    /**
     * 插入
     *
     * @param entity       实体类
     * @param updateFilter 过滤器
     * @return 主键 ID (无主键则为 null)
     */
    ID insert(Entity entity, UpdateFilter updateFilter);

    /**
     * 批量插入
     *
     * @param entityList   实体类
     * @param updateFilter 过滤器
     * @return 主键 ID 列表
     */
    List<ID> insertBatch(Collection<Entity> entityList, UpdateFilter updateFilter);

    /**
     * 查询
     *
     * @param query        查询条件
     * @param selectFilter 过滤器
     * @return 数据列表
     */
    List<Entity> select(Query query, SelectFilter selectFilter);

    /**
     * 更新
     *
     * @param entity       需要更新的数据
     * @param query        查询条件
     * @param updateFilter 过滤器
     * @return 更新成功的条数
     */
    long update(Entity entity, Query query, UpdateFilter updateFilter);

    /**
     * 删除
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

    Class<Entity> _entityClass();

}
