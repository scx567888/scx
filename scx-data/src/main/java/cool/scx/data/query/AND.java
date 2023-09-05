package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.QueryBuilder.query;
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
    protected Query getQuery() {
        return query().where(this);
    }

}
