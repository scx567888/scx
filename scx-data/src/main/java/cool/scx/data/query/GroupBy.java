package cool.scx.data.query;

import java.util.Arrays;

/**
 * 分组
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupBy extends LazyQuery {

    /**
     * 分组字段列表
     */
    private Object[] groupByClauses;

    /**
     * 创建一个 OrderBy 对象
     */
    public GroupBy() {
        this.groupByClauses = new Object[]{};
    }

    /**
     * 根据旧的 GroupBy 创建一个 GroupBy 对象
     *
     * @param oldGroupBy 旧的 GroupBy
     */
    public GroupBy(GroupBy oldGroupBy) {
        this.groupByClauses = Arrays.copyOf(oldGroupBy.groupByClauses, oldGroupBy.groupByClauses.length);
    }

    /**
     * set
     *
     * @param groupByClauses a
     * @return a
     */
    public GroupBy set(Object... groupByClauses) {
        this.groupByClauses = groupByClauses;
        return this;
    }

    public Object[] clauses() {
        return this.groupByClauses;
    }

    /**
     * clear
     *
     * @return self
     */
    public GroupBy clear() {
        this.groupByClauses = new Object[]{};
        return this;
    }

    @Override
    protected QueryImpl getQuery() {
        return new QueryImpl(this);
    }

}
