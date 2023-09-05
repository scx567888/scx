package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.QueryBuilder.query;
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
    protected Query getQuery() {
        return query().where(this);
    }

}
