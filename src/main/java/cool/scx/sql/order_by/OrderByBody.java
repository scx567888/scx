package cool.scx.sql.order_by;

import cool.scx.util.CaseUtils;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 1.11.8
 */
final class OrderByBody {

    private final String name;

    private final String orderByClause;

    /**
     * <p>Constructor for OrderByBody.</p>
     *
     * @param _name           a {@link java.lang.String} object
     * @param orderByType     a {@link cool.scx.sql.order_by.OrderByType} object
     * @param useOriginalName a boolean
     */
    OrderByBody(String _name, OrderByType orderByType, boolean useOriginalName) {
        this.name = _name.trim();
        var columnName = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
        this.orderByClause = columnName + " " + orderByType.name();
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
     * <p>orderByClause.</p>
     *
     * @return a {@link java.lang.String} object
     */
    String orderByClause() {
        return orderByClause;
    }

}
