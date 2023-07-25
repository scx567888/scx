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
    private Object[] orderByClauses;

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderBy() {
        this.orderByClauses = new Object[]{};
    }

    /**
     * 根据旧的 OrderBy 创建一个 OrderBy 对象
     *
     * @param oldOrderBy 旧的 OrderBy
     */
    public OrderBy(OrderBy oldOrderBy) {
        this.orderByClauses = Arrays.copyOf(oldOrderBy.orderByClauses, oldOrderBy.orderByClauses.length);
    }

    /**
     * set
     *
     * @param orderByClauses a
     */
    public void set(Object... orderByClauses) {
        this.orderByClauses = orderByClauses;
    }

    public Object[] clauses() {
        return this.orderByClauses;
    }

    /**
     * clear
     *
     * @return self
     */
    public OrderBy clear() {
        this.orderByClauses = new Object[]{};
        return this;
    }

}
