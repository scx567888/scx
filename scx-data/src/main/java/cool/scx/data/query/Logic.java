package cool.scx.data.query;

public abstract class Logic {

    protected final Object[] clauses;

    public Logic(Object... clauses) {
        this.clauses = clauses;
    }

    public static Logic and(Object... clauses) {
        return new AND(clauses);
    }

    public static Logic or(Object... clauses) {
        return new OR(clauses);
    }

    public abstract String keyWord();

    public final Object[] clauses() {
        return clauses;
    }

}
