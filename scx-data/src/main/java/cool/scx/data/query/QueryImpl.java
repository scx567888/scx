package cool.scx.data.query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/// QueryImpl
///
/// @author scx567888
/// @version 0.0.1
public class QueryImpl implements Query {

    private final List<OrderBy> orderBy;
    private Object where;
    private Long offset;
    private Long limit;

    public QueryImpl() {
        this.where = null;
        this.orderBy = new ArrayList<>();
        this.offset = null;
        this.limit = null;
    }

    public QueryImpl(Query oldQuery) {
        this();
        where(oldQuery.getWhere());
        addOrderBy(oldQuery.getOrderBy());
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
    public Object getWhere() {
        return where;
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
        where = null;
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

    @Override
    public QueryImpl addOrderBy(Object... orderByClauses) {
        for (var orderByClause : orderByClauses) {
            if (orderByClause == null) {
                continue;
            }
            if (orderByClause instanceof Object[] objs) {
                addOrderBy(objs);
                continue;
            }
            if (orderByClause instanceof OrderBy w && w.info().replace()) {
                orderBy.removeIf(c -> c instanceof OrderBy w1 && w1.name().equals(w.name()));
            }
            orderBy.add(orderByClause);
        }
        return this;
    }

    @Override
    public Query removeOrderByIf(Predicate<Object> filter) {
        orderBy.removeIf(filter);
        return this;
    }

}
