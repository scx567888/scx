package cool.scx.data.query;

import cool.scx.data.Query;

import static cool.scx.data.Query.where;

public interface Logic extends Query {

    LogicType type();

    Object[] clauses();

    @Override
    default Where getWhere() {
        return where(this);
    }

}
