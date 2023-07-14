package cool.scx.data.query;

import cool.scx.data.query.OrderByOption.Info;

import java.util.Arrays;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;

/**
 * 排序
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class OrderBy {

    /**
     * 存储排序的字段
     */
    private OrderByBody[] orderByBodyList;

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderBy() {
        this.orderByBodyList = new OrderByBody[]{};
    }

    /**
     * 根据旧的 OrderBy 创建一个 OrderBy 对象
     *
     * @param oldOrderBy 旧的 OrderBy
     */
    public OrderBy(OrderBy oldOrderBy) {
        this.orderByBodyList = Arrays.stream(oldOrderBy.orderByBodyList()).toArray(OrderByBody[]::new);
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

    public void set(OrderByBody... orderByClauses) {
        orderByBodyList = orderByClauses;
    }

    /**
     * orderByBodyList
     *
     * @return orderByBodyList
     */
    public OrderByBody[] orderByBodyList() {
        return orderByBodyList;
    }

    /**
     * clear
     *
     * @return self
     */
    public OrderBy clear() {
        orderByBodyList = new OrderByBody[]{};
        return this;
    }

}
