package cool.scx.dao.group_by;

import cool.scx.sql.mapping.TableInfo;

import java.util.ArrayList;
import java.util.List;

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
    private final List<GroupByBody> groupByBodyList;

    /**
     * 创建一个 OrderBy 对象
     */
    public GroupBy() {
        this.groupByBodyList = new ArrayList<>();
    }

    /**
     * 根据旧的 GroupBy 创建一个 GroupBy 对象
     *
     * @param oldGroupBy 旧的 GroupBy
     */
    public GroupBy(GroupBy oldGroupBy) {
        this.groupByBodyList = new ArrayList<>(oldGroupBy.groupByBodyList);
    }

    /**
     * 添加一个 分组字段
     *
     * @param name    分组字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param options 配置
     * @return 本身, 方便链式调用
     */
    public GroupBy add(String name, GroupByOption... options) {
        var info = new GroupByOption.Info(options);
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
    public String[] getGroupByColumns(TableInfo<?> tableInfo) {
        //此处去重
        return groupByBodyList.stream().map(c -> c.groupByColumn(tableInfo)).distinct().toArray(String[]::new);
    }

}
