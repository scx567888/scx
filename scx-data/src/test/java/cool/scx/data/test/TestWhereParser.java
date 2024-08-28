package cool.scx.data.test;

import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.parser.WhereParser;

class TestWhereParser extends WhereParser {

    @Override
    public WhereClause parseJsonContains(Where w) {
        return parseEqual(w);
    }

    @Override
    public WhereClause parseBetween(Where w) {
        return parseEqual(w);
    }

    @Override
    public WhereClause parseIn(Where w) {
        return parseEqual(w);
    }

    @Override
    public WhereClause parseLike(Where w) {
        return parseEqual(w);
    }

    @Override
    public WhereClause parseEqual(Where w) {
        return new WhereClause(w.name() + " " + getWhereKeyWord(w.whereType()) + " ?", w.value1());
    }

    @Override
    public WhereClause parseIsNull(Where w) {
        return parseEqual(w);
    }

}
