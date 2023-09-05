package cool.scx.data.query;

import cool.scx.data.Query;

/**
 * 查询参数类<br>
 * 针对  GroupBy , OrderBy , Limit , Where 等进行的简单封装 <br>
 * 同时附带一些简单的参数校验 <br>
 * 只是 为了方便传递参数使用<br>
 *
 * @author scx567888
 * @version 0.1.3
 */
public class QueryImpl implements Query {

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
    private final LimitInfo limitInfo;

    /**
     * 创建 Query 对象
     */
    public QueryImpl() {
        this.where = new Where();
        this.groupBy = new GroupBy();
        this.orderBy = new OrderBy();
        this.limitInfo = new LimitInfo();
    }

    public QueryImpl(Query oldQuery) {
        this.where = new Where(oldQuery.getWhere());
        this.groupBy = new GroupBy(oldQuery.getGroupBy());
        this.orderBy = new OrderBy(oldQuery.getOrderBy());
        this.limitInfo = new LimitInfo(oldQuery.getLimitInfo());
    }

    QueryImpl(Where oldWhere) {
        this(oldWhere, new GroupBy(), new OrderBy(), new LimitInfo());
    }

    QueryImpl(GroupBy oldGroupBy) {
        this(new Where(), oldGroupBy, new OrderBy(), new LimitInfo());
    }

    QueryImpl(OrderBy oldOrderBy) {
        this(new Where(), new GroupBy(), oldOrderBy, new LimitInfo());
    }

    QueryImpl(LimitInfo oldLimitInfo) {
        this(new Where(), new GroupBy(), new OrderBy(), oldLimitInfo);
    }

    private QueryImpl(Where oldWhere, GroupBy oldGroupBy, OrderBy oldOrderBy, LimitInfo oldLimitInfo) {
        this.where = oldWhere;
        this.groupBy = oldGroupBy;
        this.orderBy = oldOrderBy;
        this.limitInfo = oldLimitInfo;
    }

    @Override
    public QueryImpl where(Object... whereClauses) {
        this.where.set(whereClauses);
        return this;
    }

    @Override
    public QueryImpl groupBy(Object... groupByClauses) {
        this.groupBy.set(groupByClauses);
        return this;
    }

    @Override
    public QueryImpl orderBy(Object... orderByClauses) {
        this.orderBy.set(orderByClauses);
        return this;
    }

    @Override
    public QueryImpl addWhere(Object... whereClauses) {
        this.where.add(whereClauses);
        return this;
    }

    @Override
    public QueryImpl addGroupBy(Object... groupByClauses) {
        this.groupBy.add(groupByClauses);
        return this;
    }

    @Override
    public QueryImpl addOrderBy(Object... orderByClauses) {
        this.orderBy.add(orderByClauses);
        return this;
    }

    @Override
    public QueryImpl offset(long limitOffset) {
        limitInfo.offset(limitOffset);
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param numberOfRows 长度
     * @return p
     */
    @Override
    public QueryImpl limit(long numberOfRows) {
        limitInfo.limit(numberOfRows);
        return this;
    }

    @Override
    public Where getWhere() {
        return where;
    }

    @Override
    public GroupBy getGroupBy() {
        return groupBy;
    }

    @Override
    public OrderBy getOrderBy() {
        return orderBy;
    }

    @Override
    public Long getOffset() {
        return limitInfo.getOffset();
    }

    @Override
    public Long getLimit() {
        return limitInfo.getLimit();
    }

    @Override
    public LimitInfo getLimitInfo() {
        return limitInfo;
    }

    @Override
    public QueryImpl clearWhere() {
        where.clear();
        return this;
    }

    @Override
    public QueryImpl clearGroupBy() {
        groupBy.clear();
        return this;
    }

    @Override
    public QueryImpl clearOrderBy() {
        orderBy.clear();
        return this;
    }

    @Override
    public QueryImpl clearOffset() {
        limitInfo.clearOffset();
        return this;
    }

    @Override
    public QueryImpl clearLimit() {
        limitInfo.clearLimit();
        return this;
    }

}
