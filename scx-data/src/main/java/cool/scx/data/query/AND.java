package cool.scx.data.query;

import static cool.scx.data.query.LogicType.AND;

record AND(Object... clauses) implements Logic {

    @Override
    public LogicType type() {
        return AND;
    }

}
