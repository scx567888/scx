package cool.scx.data.query;

public interface Logic {

    static Logic and(Object... clauses) {
        return new AND(clauses);
    }

    static Logic or(Object... clauses) {
        return new OR(clauses);
    }

    LogicType type();

    Object[] clauses();

}
