package cool.scx.data.query;

import cool.scx.data.Query;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * 分组
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupBy extends QueryLike<GroupBy> {

    /**
     * 分组字段列表
     */
    private final List<Object> clauses;

    /**
     * 创建一个 OrderBy 对象
     */
    public GroupBy() {
        this.clauses = new ArrayList<>();
    }

    /**
     * 根据旧的 GroupBy 创建一个 GroupBy 对象
     *
     * @param oldGroupBy 旧的 GroupBy
     */
    public GroupBy(GroupBy oldGroupBy) {
        this.clauses = new ArrayList<>(oldGroupBy.clauses);
    }

    public GroupBy set(Object... groupByClauses) {
        this.clear();
        this.add(groupByClauses);
        return this;
    }

    public GroupBy add(Object... groupByClauses) {
        addAll(this.clauses, groupByClauses);
        return this;
    }

    public Object[] clauses() {
        return this.clauses.toArray();
    }

    public GroupBy clear() {
        this.clauses.clear();
        return this;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl(this);
    }

}
