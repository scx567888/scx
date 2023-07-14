package cool.scx.data.query;

import java.util.Arrays;

/**
 * 分组
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupBy {

    /**
     * 分组字段列表
     */
    private Object[] groupByBodyList;

    /**
     * 创建一个 OrderBy 对象
     */
    public GroupBy() {
        this.groupByBodyList = new Object[]{};
    }

    /**
     * 根据旧的 GroupBy 创建一个 GroupBy 对象
     *
     * @param oldGroupBy 旧的 GroupBy
     */
    public GroupBy(GroupBy oldGroupBy) {
        this.groupByBodyList = Arrays.copyOf(oldGroupBy.groupByBodyList, oldGroupBy.groupByBodyList.length);
    }

    /**
     * set
     *
     * @param groupByClauses a
     * @return a
     */
    public GroupBy set(Object... groupByClauses) {
        groupByBodyList = groupByClauses;
        return this;
    }

    /**
     * groupByBodyList
     *
     * @return groupByBodyList
     */
    public Object[] groupByBodyList() {
        return groupByBodyList;
    }

    /**
     * clear
     *
     * @return self
     */
    public GroupBy clear() {
        groupByBodyList = new Object[]{};
        return this;
    }

}
