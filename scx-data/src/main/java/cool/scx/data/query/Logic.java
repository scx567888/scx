package cool.scx.data.query;

public interface Logic extends ClauseSet<Object> {

    static Logic and(Object... clauses) {
        return new AND(clauses);
    }

    static Logic or(Object... clauses) {
        return new OR(clauses);
    }

    static WhereBodySet andSet() {
        return new WhereBodySet(LogicType.AND);
    }

    static WhereBodySet orSet() {
        return new WhereBodySet(LogicType.OR);
    }

    LogicType type();

}
