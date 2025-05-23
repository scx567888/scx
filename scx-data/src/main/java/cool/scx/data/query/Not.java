package cool.scx.data.query;

public class Not extends QueryLike<Not> implements Where {

    private final Object clause;

    public Not(Object clause) {
        this.clause = clause;
    }

    public Object clause() {
        return clause;
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().where(this);
    }

}
