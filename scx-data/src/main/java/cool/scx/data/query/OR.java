package cool.scx.data.query;

import static cool.scx.data.query.LogicType.OR;

public final class OR extends LazyQuery implements Logic {

    private final Object[] clauses;

    public OR(Object... clauses) {
        this.clauses = clauses;
    }

    @Override
    public LogicType type() {
        return OR;
    }

    @Override
    public Object[] clauses() {
        return clauses;
    }

    @Override
    protected QueryImpl convertToQuery() {
        return new QueryImpl().where(this);
    }

}
