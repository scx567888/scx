package cool.scx.data;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.Limit;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Where;

public interface Query {

    static QueryImpl query() {
        return new QueryImpl();
    }

    static QueryImpl query(QueryImpl oldQuery) {
        return new QueryImpl(oldQuery);
    }

    static Where where(Object... whereClauses) {
        return new Where().set(whereClauses);
    }

    static GroupBy groupBy(Object... groupByClauses) {
        return new GroupBy().set(groupByClauses);
    }

    static OrderBy orderBy(Object... orderByClauses) {
        return new OrderBy().set(orderByClauses);
    }

    static Limit offset(long limitOffset) {
        return new Limit().offset(limitOffset);
    }

    static Limit limit(long numberOfRows) {
        return new Limit().limit(numberOfRows);
    }

    default Where getWhere() {
        return new Where();
    }

    default GroupBy getGroupBy() {
        return new GroupBy();
    }

    default OrderBy getOrderBy() {
        return new OrderBy();
    }

    default Long getOffset() {
        return null;
    }

    default Long getLimit() {
        return null;
    }


}
