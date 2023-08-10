package cool.scx.data;

import cool.scx.data.query.*;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;

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

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    static OrderByBody asc(String name, OrderByOption... options) {
        return new OrderByBody(name, ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    static OrderByBody desc(String name, OrderByOption... options) {
        return new OrderByBody(name, DESC, options);
    }

    static OrderByBodySet orderBySet() {
        return new OrderByBodySet();
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
