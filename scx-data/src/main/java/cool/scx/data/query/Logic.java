package cool.scx.data.query;

public interface Logic {

    static Logic and(Object... clauses) {
        return new AND(clauses);
    }

    static Logic or(Object... clauses) {
        return new OR(clauses);
    }

    static WhereSet andSet() {
        return new WhereSet(LogicType.AND);
    }

    static WhereSet orSet() {
        return new WhereSet(LogicType.OR);
    }

    LogicType type();

    Object[] clauses();

}
