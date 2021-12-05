package cool.scx.sql.order_by;

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
     * @return 本身, 方便链式调用
     */
    public OrderBy add(String name, OrderByType orderByType, OrderByOption... options) {
        if (name == null) {
            throw new RuntimeException(" OrderBy 类型 : " + orderByType + " , 字段名称不能为空 !!!");
        }
        return _add(name.trim(), orderByType, options);
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public OrderBy asc(String name, OrderByOption... options) {
        return add(name.trim(), OrderByType.ASC, options);
    }


    /**
     * a
     *
     * @param name a
     * @return a
     */
    public OrderBy desc(String name, OrderByOption... options) {
        return add(name.trim(), OrderByType.DESC, options);
    }

    /**
     * a
     *
     * @param name        a
     * @param orderByType a
     * @param options     a
     * @return a
     */
    private OrderBy _add(String name, OrderByType orderByType, OrderByOption... options) {
        var replace = false;
        var useOriginalName = false;
        for (var option : options) {
            if (option == OrderByOption.REPLACE) {
                replace = true;
            } else if (option == OrderByOption.USE_ORIGINAL_NAME) {
                useOriginalName = true;
            }
        }
        // 是否使用原始名称 (即不进行转义)
        var orderByBody = new OrderByBody(name, orderByType, useOriginalName);
        // 是否替换
        if (replace) {
            orderByBodyList.removeIf(w -> orderByBody.name().equals(w.name()));
        }
        orderByBodyList.add(orderByBody);
        return this;
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
