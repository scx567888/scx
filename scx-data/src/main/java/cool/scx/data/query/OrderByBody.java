package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.common.util.StringUtils.isBlank;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class OrderByBody extends QueryLike<OrderByBody> {

    private final String name;
    private final OrderByType orderByType;
    private final OrderByOption option;

    /**
     * 添加一个排序字段
     *
     * @param name        排序字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType 排序类型 正序或倒序
     * @param options     配置
     */
    public OrderByBody(String name, OrderByType orderByType, OrderByOption... options) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name.trim();
        this.orderByType = orderByType;
        this.option = options != null && options.length > 0 && options[0] != null ? options[0] : new OrderByOption();
    }

    public String name() {
        return name;
    }

    public OrderByType orderByType() {
        return orderByType;
    }

    public OrderByOption option() {
        return option;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().orderBy(this);
    }

}
