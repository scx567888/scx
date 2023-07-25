package cool.scx.data.query;

import cool.scx.data.ReadableQuery;

import static cool.scx.data.query.OrderBy.orderBy;
import static cool.scx.data.query.OrderByOption.Info;
import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;
import static cool.scx.util.StringUtils.isBlank;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public record OrderByBody(String name, OrderByType orderByType, Info info) implements ReadableQuery {

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
     * @return 本身, 方便链式调用
     */
    public static OrderByBody of(String name, OrderByType orderByType, OrderByOption... options) {
        return new OrderByBody(name, orderByType, new Info(options));
    }

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderByBody asc(String name, OrderByOption... options) {
        return of(name, ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderByBody desc(String name, OrderByOption... options) {
        return of(name, DESC, options);
    }

    @Override
    public OrderBy getOrderBy() {
        return orderBy(this);
    }

}
