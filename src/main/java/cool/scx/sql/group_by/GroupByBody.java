package cool.scx.sql.group_by;

import cool.scx.sql.SQLHelper;
import cool.scx.util.StringUtils;

/**
 * <p>GroupByBody class.</p>
 *
 * @author scx567888
 * @version 1.11.8
 */
final class GroupByBody {

    private final String name;

    private final String groupByColumn;

    /**
     * <p>Constructor for GroupByBody.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param info a boolean
     */
    GroupByBody(String name, GroupByOption.Info info) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name.trim();
        this.groupByColumn = SQLHelper.getColumnName(this.name, info.useJsonExtract(), info.useOriginalName());
    }

    /**
     * <p>name.</p>
     *
     * @return a {@link java.lang.String} object
     */
    String name() {
        return name;
    }

    /**
     * <p>groupByColumn.</p>
     *
     * @return a {@link java.lang.String} object
     */
    String groupByColumn() {
        return groupByColumn;
    }

}
