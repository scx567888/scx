package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.query.LogicType.AND;

public final class AND extends QueryLike<AND> implements Logic {

    private final Object[] clauses;

    public AND(Object... clauses) {
        this.clauses = clauses;
    }

    @Override
    public LogicType logicType() {
        return AND;
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
