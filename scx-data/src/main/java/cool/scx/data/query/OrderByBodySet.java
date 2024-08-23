package cool.scx.data.query;

import cool.scx.data.Query;

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
    private final List<OrderByBody> clauses;

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderByBodySet() {
        this.clauses = new ArrayList<>();
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
        var option = options != null && options.length > 0 && options[0] != null ? options[0] : new OrderByOption();
        // 是否使用原始名称 (即不进行转义)
        var orderByBody = new OrderByBody(name, orderByType, option);
        // 是否替换
        if (option.replace()) {
            clauses.removeIf(w -> orderByBody.name().equals(w.name()));
        }
        clauses.add(orderByBody);
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
        clauses.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * clear
     *
     * @return self
     */
    public OrderByBodySet clear() {
        clauses.clear();
        return this;
    }

    /**
     * orderByBodyList
     *
     * @return orderByBodyList
     */
    public Object[] clauses() {
        return clauses.toArray();
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().orderBy(this);
    }

}
