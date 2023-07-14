package cool.scx.data.query;

import java.util.Arrays;

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
        this.orderByBodyList = Arrays.copyOf(oldOrderBy.orderByBodyList, oldOrderBy.orderByBodyList.length);
    }

    /**
     * set
     *
     * @param orderByClauses a
     */
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
