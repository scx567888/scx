package cool.scx.data.query;

import static cool.scx.data.query.LogicType.AND;

public final class AND extends LazyQuery implements Logic {

    private final Object[] clauses;

    public AND(Object... clauses) {
        this.clauses = clauses;
    }

    @Override
    public LogicType type() {
        return AND;
    }

    @Override
    public Object[] clauses() {
        return clauses;
    }

    @Override
    protected QueryImpl getQuery() {
        return new QueryImpl().where(this);
    }

}
