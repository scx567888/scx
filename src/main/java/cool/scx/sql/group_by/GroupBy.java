package cool.scx.sql.group_by;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组
 *
 * @author scx567888
 * @version 1.2.0
 */
public final class GroupBy {

    /**
     * 分组字段列表
     */
    private final List<GroupByBody> groupByBodyList = new ArrayList<>();

    /**
     * 创建一个 OrderBy 对象
     */
    public GroupBy() {

    }

    /**
     * 添加一个 分组字段
     *
     * @param name    分组字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param options 配置
     * @return 本身, 方便链式调用
     */
    public GroupBy add(String name, GroupByOption... options) {
        var info = new GroupByOptionInfo(options);
        var groupByBody = new GroupByBody(name, info);
        groupByBodyList.add(groupByBody);
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public GroupBy remove(String name) {
        groupByBodyList.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public GroupBy clear() {
        groupByBodyList.clear();
        return this;
    }

    /**
     * <p>getGroupByColumns.</p>
     *
     * @return an array of {@link java.lang.String} objects
     */
    public String[] getGroupByColumns() {
        //此处去重
        return groupByBodyList.stream().map(GroupByBody::groupByColumn).distinct().toArray(String[]::new);
    }

}
