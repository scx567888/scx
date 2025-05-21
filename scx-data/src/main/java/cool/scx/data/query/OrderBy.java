package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

/// OrderBy
///
/// @author scx567888
/// @version 0.0.1
public final class OrderBy extends QueryLike<OrderBy> {

    // fieldName 或者 表达式
    private final String selector;
    private final OrderByType orderByType;
    private final BuildControlInfo info;

    public OrderBy(String selector, OrderByType orderByType, BuildControlInfo info) {
        if (isBlank(selector)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : selector 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.selector = selector;
        this.orderByType = orderByType;
        this.info = info;
    }

    /// 添加一个排序字段
    ///
    /// @param selector    selector
    /// @param orderByType 排序类型 正序或倒序
    /// @param controls    配置
    public OrderBy(String selector, OrderByType orderByType, BuildControl... controls) {
        this(selector, orderByType, ofInfo(controls));
    }

    public String selector() {
        return selector;
    }

    public OrderByType orderByType() {
        return orderByType;
    }

    public BuildControlInfo info() {
        return info;
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().orderBys(this);
    }

}
