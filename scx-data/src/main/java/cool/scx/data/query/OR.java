package cool.scx.data.query;

import static cool.scx.data.query.LogicType.OR;

public record OR(Object... clauses) implements Logic {

    @Override
    public LogicType type() {
        return OR;
    }

}
