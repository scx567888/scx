package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;

/// OrderBy
///
/// @author scx567888
/// @version 0.0.1
public final class OrderBy extends QueryLike<OrderBy> {

    // fieldName 或者 表达式
    private final String selector;
    private final OrderByType orderByType;
    private final boolean useExpression;

    public OrderBy(String selector, OrderByType orderByType, boolean useExpression) {
        if (isBlank(selector)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : selector 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.selector = selector;
        this.orderByType = orderByType;
        this.useExpression = useExpression;
    }

    public String selector() {
        return selector;
    }

    public OrderByType orderByType() {
        return orderByType;
    }

    public boolean useExpression() {
        return useExpression;
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().orderBys(this);
    }

}
