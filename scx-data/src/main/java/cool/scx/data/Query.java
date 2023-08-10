package cool.scx.data;

import cool.scx.data.query.*;

public interface Query {

    static QueryImpl query() {
        return new QueryImpl();
    }

    static QueryImpl query(Query oldQuery) {
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

    static LimitInfo offset(long limitOffset) {
        return new LimitInfo().offset(limitOffset);
    }

    static LimitInfo limit(long numberOfRows) {
        return new LimitInfo().limit(numberOfRows);
    }

    static Logic and(Object... clauses) {
        return new AND(clauses);
    }

    static Logic or(Object... clauses) {
        return new OR(clauses);
    }

    static WhereBodySet andSet() {
        return new WhereBodySet(LogicType.AND);
    }

    static WhereBodySet orSet() {
        return new WhereBodySet(LogicType.OR);
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

    default LimitInfo getLimitInfo() {
        return new LimitInfo();
    }

}
