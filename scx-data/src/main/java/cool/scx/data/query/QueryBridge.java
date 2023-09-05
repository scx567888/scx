package cool.scx.data.query;

import cool.scx.data.Query;

public interface QueryBridge extends Query {

    @Override
    default Query where(Object... whereClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query groupBy(Object... groupByClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query orderBy(Object... orderByClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query addWhere(Object... whereClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query addGroupBy(Object... groupByClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query addOrderBy(Object... orderByClauses) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query offset(long limitOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Query limit(long numberOfRows) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Where getWhere() {
        return new Where();
    }

    @Override
    default GroupBy getGroupBy() {
        return new GroupBy();
    }

    @Override
    default OrderBy getOrderBy() {
        return new OrderBy();
    }

    @Override
    default Long getOffset() {
        return null;
    }

    @Override
    default Long getLimit() {
        return null;
    }

    @Override
    default LimitInfo getLimitInfo() {
        return new LimitInfo();
    }

    @Override
    default Query clearWhere() {
        return this;
    }

    @Override
    default Query clearGroupBy() {
        return this;
    }

    @Override
    default Query clearOrderBy() {
        return this;
    }

    @Override
    default Query clearOffset() {
        return this;
    }

    @Override
    default Query clearLimit() {
        return this;
    }

}
