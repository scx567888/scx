package cool.scx.sql.order_by;

import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序
 *
 * @author scx567888
 * @version 1.2.0
 */
public final class OrderBy {

    /**
     * 存储排序的字段
     */
    private final List<OrderByBody> orderByBodyList = new ArrayList<>();

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderBy() {

    }

    /**
     * 添加一个排序字段
     *
     * @param name        排序字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType 排序类型 正序或倒序
     * @param options     配置
     * @return 本身, 方便链式调用
     */
    public OrderBy add(String name, OrderByType orderByType, OrderByOption... options) {
        //校验参数
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        var info = new OrderByOptionInfo(options);
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
    public OrderBy asc(String name, OrderByOption... options) {
        return add(name.trim(), OrderByType.ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public OrderBy desc(String name, OrderByOption... options) {
        return add(name.trim(), OrderByType.DESC, options);
    }

    /**
     * <p>getOrderByClauses.</p>
     *
     * @return an array of {@link java.lang.String} objects
     */
    public String[] getOrderByClauses() {
        return orderByBodyList.stream().map(OrderByBody::orderByClause).toArray(String[]::new);
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public OrderBy remove(String name) {
        orderByBodyList.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public OrderBy clear() {
        orderByBodyList.clear();
        return this;
    }

}
