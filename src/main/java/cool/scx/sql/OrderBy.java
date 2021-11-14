package cool.scx.sql;

import cool.scx.util.CaseUtils;
import cool.scx.util.ansi.Ansi;

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
    private final List<OrderByBody> orderByList = new ArrayList<>();

    /**
     * 创建一个 OrderBy 对象
     */
    public OrderBy() {

    }

    /**
     * c
     *
     * @param orderByColumn a
     * @param orderByType   a
     */
    public OrderBy(String orderByColumn, OrderByType orderByType) {
        add(orderByColumn, orderByType);
    }

    /**
     * 添加一个排序字段
     *
     * @param orderByColumn 排序字段的名称 (注意是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType   排序类型 正序或倒序
     * @return 本身, 方便链式调用
     */
    public OrderBy add(String orderByColumn, OrderByType orderByType) {
        orderByList.add(new OrderByBody(orderByColumn.trim(), orderByType, false));
        return this;
    }

    public OrderBy asc(String orderByColumn) {
        return add(orderByColumn, OrderByType.ASC);
    }

    public OrderBy ascSQL(String orderBySQL) {
        return addSQL(orderBySQL, OrderByType.ASC);
    }

    public OrderBy desc(String orderByColumn) {
        return add(orderByColumn, OrderByType.DESC);
    }

    public OrderBy descSQL(String orderBySQL) {
        return addSQL(orderBySQL, OrderByType.DESC);
    }

    /**
     * 添加一个排序字段
     *
     * @param orderByColumn 排序字段的名称 (注意是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByStr    排序类型 正序或倒序
     * @return 本身, 方便链式调用
     */
    public OrderBy add(String orderByColumn, String orderByStr) {
        if ("ASC".equalsIgnoreCase(orderByStr.trim())) {
            return add(orderByColumn, OrderByType.ASC);
        } else if ("DESC".equalsIgnoreCase(orderByStr.trim())) {
            return add(orderByColumn, OrderByType.DESC);
        } else {
            Ansi.out().brightRed("排序类型有误 : " + orderByStr + " , 排序字段名称 : " + orderByColumn + " , 只能是 asc 或 desc (不区分大小写) !!!").println();
            return this;
        }
    }

    /**
     * 添加一个排序 SQL
     *
     * @param orderBySQL  排序 SQL ( SQL 表达式 )
     * @param orderByType 排序类型 正序或倒序
     * @return 本身, 方便链式调用
     */
    public OrderBy addSQL(String orderBySQL, OrderByType orderByType) {
        orderByList.add(new OrderByBody(orderBySQL, orderByType, true));
        return this;
    }

    /**
     * 添加一个排序 SQL
     *
     * @param orderBySQL 排序 SQL ( SQL 表达式 )
     * @param orderByStr 排序类型 正序或倒序
     * @return 本身, 方便链式调用
     */
    public OrderBy addSQL(String orderBySQL, String orderByStr) {
        if ("ASC".equalsIgnoreCase(orderByStr.trim())) {
            return addSQL(orderBySQL, OrderByType.ASC);
        } else if ("DESC".equalsIgnoreCase(orderByStr.trim())) {
            return addSQL(orderBySQL, OrderByType.DESC);
        } else {
            Ansi.out().brightRed("排序类型有误 : " + orderByStr + " , 排序字段名称 : " + orderBySQL + " , 只能是 asc 或 desc (不区分大小写) !!!").println();
            return this;
        }
    }

    /**
     * <p>getOrderByClauses.</p>
     *
     * @return an array of {@link java.lang.String} objects
     */
    String[] getOrderByClauses() {
        return orderByList.stream()
                .map((entry) -> (entry.isSQL() ? entry.orderByColumn() : CaseUtils.toSnake(entry.orderByColumn())) + " " + entry.orderByType().name())
                .toArray(String[]::new);
    }

}
