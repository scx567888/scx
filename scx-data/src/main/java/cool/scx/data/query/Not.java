package cool.scx.data.query;

public class Not extends QueryLike<Not> {

    private final Object clause;

    public Not(Object clause) {
        this.clause = clause;
    }

    public Object clause() {
        return clause;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
