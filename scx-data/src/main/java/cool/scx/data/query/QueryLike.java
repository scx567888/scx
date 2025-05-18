package cool.scx.data.query;

import java.util.function.Predicate;

/// QueryLike
///
/// @author scx567888
/// @version 0.0.1
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
    public QL where(Object where) {
        query().where(where);
        return (QL) this;
    }

    @Override
    public QL orderBy(Object... orderByClauses) {
        query().orderBy(orderByClauses);
        return (QL) this;
    }

    @Override
    public QL offset(long offset) {
        query().offset(offset);
        return (QL) this;
    }

    @Override
    public QL limit(long limit) {
        query().limit(limit);
        return (QL) this;
    }

    @Override
    public Object getWhere() {
        return query().getWhere();
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

    @Override
    public QL addOrderBy(Object... orderByClauses) {
        query().addOrderBy(orderByClauses);
        return (QL) this;
    }

    @Override
    public QL removeOrderByIf(Predicate<Object> filter) {
        query().removeOrderByIf(filter);
        return (QL) this;
    }

    protected abstract Query toQuery();

}
