package cool.scx.base;

import cool.scx.ScxContext;
import cool.scx.sql.where.WhereOption;

final class BaseModelServiceHelper {

    /**
     * 处理墓碑机制的数据
     * <br>
     * 如果没有开启墓碑机制 : 不做任何处理
     * <br>
     * 如果开启墓碑机制 做以下处理
     * 1, 在查询条件强制添加 tombstone 字段值等于 false
     * <br>
     * 2, 根据不同的 selectFilter 类型进行查询参数过滤 隐藏数据库中所有 tombstone 字段的信息
     *
     * @param query q
     */
    static Query queryProcessorForTombstone(Query query) {
        if (ScxContext.easyConfig().tombstone()) {
            query.equal("tombstone", false, WhereOption.REPLACE);
        }
        return query;
    }

    /**
     * 当 启用逻辑删除时 则不允许查询 tombstone 值 所以再次进行处理
     *
     * @param selectFilter s
     */
    static SelectFilter selectFilterProcessorForTombstone(SelectFilter selectFilter) {
        if (ScxContext.easyConfig().tombstone()) {
            selectFilter.addExcluded("tombstone");
        }
        return selectFilter;
    }

    /**
     * 处理 updateFilter  使在插入或更新数据时永远过滤 "id", "updateDate", "createDate", "tombstone" 四个字段
     *
     * @param updateFilter u
     */
    static UpdateFilter updateFilterProcessor(UpdateFilter updateFilter) {
        return updateFilter.addExcluded("id", "updateDate", "createDate", "tombstone");
    }

}
