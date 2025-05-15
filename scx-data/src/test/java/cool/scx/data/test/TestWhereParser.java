package cool.scx.data.test;

import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.WhereType;
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
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            }
        }
        return new WhereClause(w.name() + " " + getWhereKeyWord(w.whereType()) + " ?", w.value1());
    }

    public String getWhereKeyWord(WhereType whereType) {
        return switch (whereType) {
            case EQ -> "=";
            case NE -> "!=";
            case LT -> "<";
            case LTE -> "<=";
            case GT -> ">";
            case GTE -> ">=";
            case LIKE, LIKE_REGEX -> "LIKE";
            case NOT_LIKE, NOT_LIKE_REGEX -> "NOT LIKE";
            case IN -> "IN";
            case NOT_IN -> "NOT IN";
            case BETWEEN -> "BETWEEN";
            case NOT_BETWEEN -> "NOT BETWEEN";
            case JSON_CONTAINS -> "JSON_CONTAINS";
            case JSON_OVERLAPS -> "JSON_OVERLAPS";
        };
    }

}
