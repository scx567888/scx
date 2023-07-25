package cool.scx.data.query;

import cool.scx.data.ReadableQuery;

public interface Logic extends ReadableQuery {

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

    Object[] clauses();

    @Override
    default Where getWhere() {
        return new Where().set(this);
    }

}
