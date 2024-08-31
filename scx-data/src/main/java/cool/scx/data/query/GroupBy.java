package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.QueryOption.Info;
import static cool.scx.data.query.QueryOption.ofInfo;

/**
 * GroupBy
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupBy extends QueryLike<GroupBy> {

    private final String name;
    private final Info info;

    public GroupBy(String name, Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name;
        this.info = info;
    }

    public GroupBy(String name, QueryOption... options) {
        this(name, ofInfo(options));
    }

    public String name() {
        return name;
    }

    public Info info() {
        return info;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().groupBy(this);
    }

}
