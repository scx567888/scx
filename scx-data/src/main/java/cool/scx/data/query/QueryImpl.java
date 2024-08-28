package cool.scx.data.query;

import cool.scx.data.Query;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

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
    private final List<Object> where;

    /**
     * 自定义分组 SQL 添加
     */
    private final List<Object> groupBy;

    /**
     * 排序的字段
     */
    private final List<Object> orderBy;

    /**
     * 当前页 页码 默认为空 即不设置页码
     */
    private Long offset;

    /**
     * 每页数量分页 每页数量 默认为空 即不设置分页内容
     */
    private Long limit;

    /**
     * 创建 Query 对象
     */
    public QueryImpl() {
        this.where = new ArrayList<>();
        this.groupBy = new ArrayList<>();
        this.orderBy = new ArrayList<>();
        this.offset = null;
        this.limit = null;
    }

    public QueryImpl(Query oldQuery) {
        this();
        addWhere(oldQuery.getWhere());
        addGroupBy(oldQuery.getWhere());
        addOrderBy(oldQuery.getWhere());
        offset(oldQuery.getOffset());
        limit(oldQuery.getLimit());
    }

    @Override
    public QueryImpl where(Object... whereClauses) {
        clearWhere();
        addWhere(whereClauses);
        return this;
    }

    @Override
    public QueryImpl groupBy(Object... groupByClauses) {
        clearGroupBy();
        addGroupBy(groupByClauses);
        return this;
    }

    @Override
    public QueryImpl orderBy(Object... orderByClauses) {
        clearOrderBy();
        addOrderBy(orderByClauses);
        return this;
    }

    @Override
    public QueryImpl offset(long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : offset (偏移量) 不能小于 0 !!!");
        }
        this.offset = offset;
        return this;
    }

    @Override
    public QueryImpl limit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : limit (行长度) 不能小于 0 !!!");
        }
        this.limit = limit;
        return this;
    }

    @Override
    public QueryImpl addWhere(Object... whereClauses) {
        addAll(where, whereClauses);
        return this;
    }

    @Override
    public QueryImpl addGroupBy(Object... groupByClauses) {
        addAll(groupBy, groupByClauses);
        return this;
    }

    @Override
    public QueryImpl addOrderBy(Object... orderByClauses) {
        addAll(orderBy, orderByClauses);
        return this;
    }

    @Override
    public Object[] getWhere() {
        return where.toArray();
    }

    @Override
    public Object[] getGroupBy() {
        return groupBy.toArray();
    }

    @Override
    public Object[] getOrderBy() {
        return orderBy.toArray();
    }

    @Override
    public Long getOffset() {
        return offset;
    }

    @Override
    public Long getLimit() {
        return limit;
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
        offset = null;
        return this;
    }

    @Override
    public QueryImpl clearLimit() {
        limit = null;
        return this;
    }

}
