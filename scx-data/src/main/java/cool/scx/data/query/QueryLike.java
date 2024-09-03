package cool.scx.data.query;

@SuppressWarnings("unchecked")
public abstract class QueryLike<QL extends QueryLike<QL>> implements Query {

    private Query query;

    private Query query() {
        if (query == null) {
            query = toQuery();
        }
        return query;
    }

    @Override
    public QL where(Object... whereClauses) {
        query().where(whereClauses);
        return (QL) this;
    }

    @Override
    public QL groupBy(Object... groupByClauses) {
        query().groupBy(groupByClauses);
        return (QL) this;
    }

    @Override
    public QL orderBy(Object... orderByClauses) {
        query().orderBy(orderByClauses);
        return (QL) this;
    }

    @Override
    public QL offset(long limitOffset) {
        query().offset(limitOffset);
        return (QL) this;
    }

    @Override
    public QL limit(long numberOfRows) {
        query().limit(numberOfRows);
        return (QL) this;
    }

    @Override
    public Object[] getWhere() {
        return query().getWhere();
    }

    @Override
    public Object[] getGroupBy() {
        return query().getGroupBy();
    }

    @Override
    public Object[] getOrderBy() {
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
    public QL clearWhere() {
        query().clearWhere();
        return (QL) this;
    }

    @Override
    public QL clearGroupBy() {
        query().clearGroupBy();
        return (QL) this;
    }

    @Override
    public QL clearOrderBy() {
        query().clearOrderBy();
        return (QL) this;
    }

    @Override
    public QL clearOffset() {
        query().clearOffset();
        return (QL) this;
    }

    @Override
    public QL clearLimit() {
        query().clearLimit();
        return (QL) this;
    }

    protected abstract Query toQuery();

}
