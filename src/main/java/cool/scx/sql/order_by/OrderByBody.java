package cool.scx.sql.order_by;

import cool.scx.sql.SQLHelper;

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
     * @param _name       a {@link String} object
     * @param orderByType a {@link OrderByType} object
     * @param info        a boolean
     */
    OrderByBody(String _name, OrderByType orderByType, OrderByOptionInfo info) {
        this.name = _name.trim();
        var columnName = SQLHelper.getColumnName(this.name, info.useJsonExtract(), info.useOriginalName());
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
