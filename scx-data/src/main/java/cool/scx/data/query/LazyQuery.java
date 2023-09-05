package cool.scx.data.query;

import cool.scx.data.Query;

abstract class LazyQuery implements Query {

    private QueryImpl query;

    protected abstract QueryImpl convertToQuery();

    private Query query() {
        if (query == null) {
            query = convertToQuery();
        }
        return query;
    }

    @Override
    public Query where(Object... whereClauses) {
        return query().where(whereClauses);
    }

    @Override
    public Query groupBy(Object... groupByClauses) {
        return query().groupBy(groupByClauses);
    }

    @Override
    public Query orderBy(Object... orderByClauses) {
        return query().orderBy(orderByClauses);
    }

    @Override
    public Query addWhere(Object... whereClauses) {
        return query().addWhere(whereClauses);
    }

    @Override
    public Query addGroupBy(Object... groupByClauses) {
        return query().addGroupBy(groupByClauses);
    }

    @Override
    public Query addOrderBy(Object... orderByClauses) {
        return query().addOrderBy(orderByClauses);
    }

    @Override
    public Query offset(long limitOffset) {
        return query().offset(limitOffset);
    }

    @Override
    public Query limit(long numberOfRows) {
        return query().limit(numberOfRows);
    }

    @Override
    public Where getWhere() {
        return query().getWhere();
    }

    @Override
    public GroupBy getGroupBy() {
        return query().getGroupBy();
    }

    @Override
    public OrderBy getOrderBy() {
        return query().getOrderBy();
    }

    @Override
    public Long getOffset() {
        return query().getOffset();
    }

    @Override
    public Long getLimit() {
        return query().getLimit();
    }

    @Override
    public LimitInfo getLimitInfo() {
        return query().getLimitInfo();
    }

    @Override
    public Query clearWhere() {
        return query().clearWhere();
    }

    @Override
    public Query clearGroupBy() {
        return query().clearGroupBy();
    }

    @Override
    public Query clearOrderBy() {
        return query().clearOrderBy();
    }

    @Override
    public Query clearOffset() {
        return query().clearOffset();
    }

    @Override
    public Query clearLimit() {
        return query().clearLimit();
    }

}
