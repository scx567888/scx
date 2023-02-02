package cool.scx.dao.order_by;

import cool.scx.sql.SQLHelper;
import cool.scx.util.StringUtils;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
final class OrderByBody {

    private final String name;

    private final String orderByClause;

    /**
     * <p>Constructor for OrderByBody.</p>
     *
     * @param name        a {@link java.lang.String} object
     * @param orderByType a {@link OrderByType} object
     * @param info        a boolean
     */
    OrderByBody(String name, OrderByType orderByType, OrderByOption.Info info) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name.trim();
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
