package cool.scx.sql.group_by;

import cool.scx.util.CaseUtils;

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
     * @param _name           a {@link java.lang.String} object
     * @param useOriginalName a boolean
     */
    GroupByBody(String _name, boolean useOriginalName) {
        this.name = _name.trim();
        this.groupByColumn = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
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
