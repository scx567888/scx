package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.Query.orderBy;
import static cool.scx.data.query.OrderByOption.Info;
import static cool.scx.util.StringUtils.isBlank;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public record OrderByBody(String name, OrderByType orderByType, Info info) implements Query {

    public OrderByBody(String name, OrderByType orderByType, Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name.trim();
        this.orderByType = orderByType;
        this.info = info;
    }

    /**
     * 添加一个排序字段
     *
     * @param name        排序字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType 排序类型 正序或倒序
     * @param options     配置
     */
    public OrderByBody(String name, OrderByType orderByType, OrderByOption... options) {
        this(name, orderByType, new Info(options));
    }

    @Override
    public OrderBy getOrderBy() {
        return orderBy(this);
    }

}
