package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;
import static java.util.Collections.addAll;

/// QueryImpl
///
/// @author scx567888
/// @version 0.0.1
public class QueryImpl implements Query {

    private Object where;
    private List<OrderBy> orderBys;
    private Long offset;
    private Long limit;

    public QueryImpl() {
        this.where = null;
        this.orderBys = new ArrayList<>();
        this.offset = null;
        this.limit = null;
    }

    public QueryImpl(Query oldQuery) {
        this();
        where(oldQuery.getWhere());
        orderBy(oldQuery.getOrderBys());
        if (oldQuery.getOffset() != null) {
            offset(oldQuery.getOffset());
        }
        if (oldQuery.getLimit() != null) {
            limit(oldQuery.getLimit());
        }
    }

    @Override
    public QueryImpl where(Object where) {
        this.where = where;
        return this;
    }

    @Override
    public QueryImpl orderBys(OrderBy... orderBys) {
        clearOrderBys();
        orderBy(orderBys);
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
    public Object getWhere() {
        return where;
    }

    @Override
    public OrderBy[] getOrderBys() {
        return orderBys.toArray(OrderBy[]::new);
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
        where = null;
        return this;
    }

    @Override
    public QueryImpl clearOrderBys() {
        orderBys.clear();
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

    @Override
    public QueryImpl orderBy(OrderBy... orderBys) {
        addAll(this.orderBys, orderBys);
        return this;
    }

    @Override
    public QueryImpl asc(String selector, BuildControl... options) {
        orderBy(new OrderBy(selector, ASC, options));
        return this;
    }

    @Override
    public QueryImpl desc(String selector, BuildControl... options) {
        orderBy(new OrderBy(selector, DESC, options));
        return this;
    }

}
