package cool.scx.data.test;

import cool.scx.data.query.WhereClause;
import cool.scx.data.query.WhereOption;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.parser.WhereParser;

class TestWhereParser extends WhereParser {

    @Override
    public WhereClause parseJsonContains(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return parseEqual(name, whereType, value1, value2, option);
    }

    @Override
    public WhereClause parseBetween(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return parseEqual(name, whereType, value1, value2, option);
    }

    @Override
    public WhereClause parseIn(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return parseEqual(name, whereType, value1, value2, option);
    }

    @Override
    public WhereClause parseLike(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return parseEqual(name, whereType, value1, value2, option);
    }

    @Override
    public WhereClause parseEqual(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return new WhereClause(name + " " + getWhereKeyWord(whereType) + " ?", value1);
    }

    @Override
    public WhereClause parseIsNull(String name, WhereType whereType, Object value1, Object value2, WhereOption option) {
        return parseEqual(name, whereType, value1, value2, option);
    }

}
