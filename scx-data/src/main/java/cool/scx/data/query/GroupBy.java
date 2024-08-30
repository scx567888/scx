package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.GroupByOption.Info;

/**
 * GroupBy
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class GroupBy {

    private final String name;
    private final Info info;

    public GroupBy(String name, Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name;
        this.info = info;
    }

    public GroupBy(String name, GroupByOption... options) {
        this(name, new Info(options));
    }

    public String name() {
        return name;
    }

    public Info info() {
        return info;
    }

}
