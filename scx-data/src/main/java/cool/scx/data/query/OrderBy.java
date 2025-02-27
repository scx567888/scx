package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.QueryOption.Info;
import static cool.scx.data.query.QueryOption.ofInfo;

/// OrderBy
///
/// @author scx567888
/// @version 0.0.1
public final class OrderBy extends QueryLike<OrderBy> {

    private final String name;
    private final OrderByType orderByType;
    private final Info info;

    public OrderBy(String name, OrderByType orderByType, Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name;
        this.orderByType = orderByType;
        this.info = info;
    }

    /// 添加一个排序字段
    ///
    /// @param name        排序字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
    /// @param orderByType 排序类型 正序或倒序
    /// @param options     配置
    public OrderBy(String name, OrderByType orderByType, QueryOption... options) {
        this(name, orderByType, ofInfo(options));
    }

    public String name() {
        return name;
    }

    public OrderByType orderByType() {
        return orderByType;
    }

    public Info info() {
        return info;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().orderBy(this);
    }

}
