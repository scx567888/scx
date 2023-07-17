package cool.scx.data;

import cool.scx.data.query.*;

/**
 * 查询参数类<br>
 * 针对  GroupBy , OrderBy , Limit , Where 等进行的简单封装 <br>
 * 同时附带一些简单的参数校验 <br>
 * 只是 为了方便传递参数使用<br>
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class Query {

    /**
     * 自定义WHERE 添加
     */
    private final Where where;

    /**
     * 自定义分组 SQL 添加
     */
    private final GroupBy groupBy;

    /**
     * 排序的字段
     */
    private final OrderBy orderBy;

    /**
     * 分页参数
     */
    private final Limit limit;

    /**
     * 创建 Query 对象
     */
    public Query() {
        this.where = new Where();
        this.groupBy = new GroupBy();
        this.orderBy = new OrderBy();
        this.limit = new Limit();
    }

    /**
     * a
     *
     * @param oldQuery a
     */
    public Query(Query oldQuery) {
        this.where = new Where(oldQuery.where);
        this.groupBy = new GroupBy(oldQuery.groupBy);
        this.orderBy = new OrderBy(oldQuery.orderBy);
        this.limit = new Limit(oldQuery.limit);
    }

    public Query where(Object... whereClauses) {
        this.where.set(whereClauses);
        return this;
    }

    public Query groupBy(Object... groupByClauses) {
        this.groupBy.set(groupByClauses);
        return this;
    }

    public Query orderBy(OrderByBody... orderByClauses) {
        this.orderBy.set(orderByClauses);
        return this;
    }

    public Query offset(long offset) {
        limit.offset(offset);
        return this;
    }

    /**
     * setRowCount
     *
     * @param rowCount rowCount (行长度)
     * @return self
     */
    public Query rowCount(long rowCount) {
        limit.rowCount(rowCount);
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param offset   偏移量
     * @param rowCount 长度
     * @return p
     */
    public Query limit(long offset, long rowCount) {
        limit.set(offset, rowCount);
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public Query clearWhere() {
        where.clear();
        return this;
    }

    public Query clearGroupBy() {
        groupBy.clear();
        return this;
    }

    public Query clearOrderBy() {
        orderBy.clear();
        return this;
    }

    public Query clearOffset() {
        limit.clearOffset();
        return this;
    }

    public Query clearRowCount() {
        limit.clearRowCount();
        return this;
    }

    public Query clearLimit() {
        limit.clear();
        return this;
    }

}
