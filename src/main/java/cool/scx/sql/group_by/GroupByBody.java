package cool.scx.sql.group_by;

import cool.scx.sql.SQLHelper;

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
     * @param _name a {@link java.lang.String} object
     * @param info  a boolean
     */
    GroupByBody(String _name, GroupByOptionInfo info) {
        this.name = _name.trim();
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
