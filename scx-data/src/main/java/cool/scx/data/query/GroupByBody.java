package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.GroupByOption.Info;

/**
 * <p>GroupByBody class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupByBody extends QueryLike<GroupByBody> {
    private final String name;
    private final Info info;

    public GroupByBody(String name, Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name.trim();
        this.info = info;
    }

    public GroupByBody(String name, GroupByOption... options) {
        this(name, new Info(options));
    }

    public String name() {
        return name;
    }

    public Info info() {
        return info;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().groupBy(this);
    }

}
