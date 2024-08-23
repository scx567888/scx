package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.common.util.StringUtils.isBlank;

/**
 * GroupByBody
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupByBody extends QueryLike<GroupByBody> {

    private final String name;
    private final GroupByOption option;

    public GroupByBody(String name, GroupByOption... options) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name.trim();
        this.option = options != null && options.length > 0 && options[0] != null ? options[0] : new GroupByOption();
    }

    public String name() {
        return name;
    }

    public GroupByOption option() {
        return option;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().groupBy(this);
    }

}
