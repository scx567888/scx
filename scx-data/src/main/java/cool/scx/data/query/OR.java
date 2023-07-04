package cool.scx.data.query;

import static cool.scx.data.query.LogicType.OR;

public final class OR extends Logic {

    public OR(Object... clauses) {
        super(clauses);
    }

    @Override
    public LogicType type() {
        return OR;
    }

}
