package cool.scx.data.query;

import cool.scx.data.Query;
import cool.scx.data.query.OrderByOption.Info;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;

/**
 * 排序
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class OrderByBodySet extends QueryLike<OrderByBodySet> {

    /**
     * 存储排序的字段
     */
    private final List<OrderByBody> orderByBodyList;

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderByBodySet() {
        this.orderByBodyList = new ArrayList<>();
    }

    /**
     * 添加一个排序字段
     *
     * @param name        排序字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType 排序类型 正序或倒序
     * @param options     配置
     * @return 本身, 方便链式调用
     */
    public OrderByBodySet add(String name, OrderByType orderByType, OrderByOption... options) {
        var info = new Info(options);
        // 是否使用原始名称 (即不进行转义)
        var orderByBody = new OrderByBody(name, orderByType, info);
        // 是否替换
        if (info.replace()) {
            orderByBodyList.removeIf(w -> orderByBody.name().equals(w.name()));
        }
        orderByBodyList.add(orderByBody);
        return this;
    }

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public OrderByBodySet asc(String name, OrderByOption... options) {
        return add(name, ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public OrderByBodySet desc(String name, OrderByOption... options) {
        return add(name, DESC, options);
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public OrderByBodySet remove(String name) {
        orderByBodyList.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * clear
     *
     * @return self
     */
    public OrderByBodySet clear() {
        orderByBodyList.clear();
        return this;
    }

    /**
     * orderByBodyList
     *
     * @return orderByBodyList
     */
    public Object[] clauses() {
        return orderByBodyList.toArray();
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().orderBy(this);
    }

}
