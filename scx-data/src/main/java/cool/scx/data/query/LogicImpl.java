package cool.scx.data.query;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class LogicImpl implements Logic {

    private final LogicType logicType;
    private final List<Object> clauses;

    public LogicImpl(LogicType logicType) {
        this.logicType = logicType;
        this.clauses = new ArrayList<>();
    }

    @Override
    public LogicType logicType() {
        return logicType;
    }

    @Override
    public Object[] clauses() {
        return clauses.toArray();
    }

    @Override
    public Logic add(Object... logicCauses) {
        addAll(clauses, logicCauses);
        return this;
    }

}
