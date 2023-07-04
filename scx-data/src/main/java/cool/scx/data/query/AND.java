package cool.scx.data.query;

import static cool.scx.data.query.LogicType.AND;

public final class AND extends Logic {

    public AND(Object... clauses) {
        super(clauses);
    }

    @Override
    public LogicType type() {
        return AND;
    }

}
