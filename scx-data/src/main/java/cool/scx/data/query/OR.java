package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.query.LogicType.OR;

public final class OR extends QueryLike<OR> implements Logic {

    private final Object[] clauses;

    public OR(Object... clauses) {
        this.clauses = clauses;
    }

    @Override
    public LogicType logicType() {
        return OR;
    }

    @Override
    public Object[] clauses() {
        return clauses;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().where(this);
    }

}
