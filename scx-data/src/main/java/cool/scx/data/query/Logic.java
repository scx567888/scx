package cool.scx.data.query;

import cool.scx.data.Query;

public interface Logic extends Query {

    LogicType logicType();

    Object[] clauses();

}
