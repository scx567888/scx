package cool.scx.sql.order_by;

import cool.scx.util.CaseUtils;

/**
 * OrderBy 封装体
 */
final class OrderByBody {

    private final String name;

    private final String orderByClause;

    /**
     *
     */
    OrderByBody(String _name, OrderByType orderByType, boolean useOriginalName) {
        this.name = _name.trim();
        var columnName = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
        this.orderByClause = columnName + " " + orderByType.name();
    }

    String name() {
        return name;
    }

    String orderByClause() {
        return orderByClause;
    }

}