package cool.scx.orm.query;

public abstract class Logic {

    protected final Object[] clauses;

    public Logic(Object... clauses) {
        this.clauses = clauses;
    }

    public abstract String keyWord();

    public final Object[] clauses() {
        return clauses;
    }

}
