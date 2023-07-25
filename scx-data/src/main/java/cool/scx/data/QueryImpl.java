package cool.scx.data;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.Limit;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Where;

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
    private final Limit limit;

    /**
     * 创建 Query 对象
     */
    public QueryImpl() {
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
    public QueryImpl(QueryImpl oldQuery) {
        this.where = new Where(oldQuery.where);
        this.groupBy = new GroupBy(oldQuery.groupBy);
        this.orderBy = new OrderBy(oldQuery.orderBy);
        this.limit = new Limit(oldQuery.limit);
    }

    public QueryImpl where(Object... whereClauses) {
        this.where.set(whereClauses);
        return this;
    }

    public QueryImpl groupBy(Object... groupByClauses) {
        this.groupBy.set(groupByClauses);
        return this;
    }

    public QueryImpl orderBy(Object... orderByClauses) {
        this.orderBy.set(orderByClauses);
        return this;
    }

    public QueryImpl offset(long limitOffset) {
        limit.offset(limitOffset);
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param numberOfRows 长度
     * @return p
     */
    public QueryImpl limit(long numberOfRows) {
        limit.limit(numberOfRows);
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
        return limit.getOffset();
    }
    
    @Override
    public Long getLimit() {
        return limit.getLimit();
    }

    public QueryImpl clearWhere() {
        where.clear();
        return this;
    }

    public QueryImpl clearGroupBy() {
        groupBy.clear();
        return this;
    }

    public QueryImpl clearOrderBy() {
        orderBy.clear();
        return this;
    }

    public QueryImpl clearOffset() {
        limit.clearOffset();
        return this;
    }

    public QueryImpl clearLimit() {
        limit.clearLimit();
        return this;
    }

}
