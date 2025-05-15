package cool.scx.data.test;

import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.parser.WhereParser;

class TestWhereParser extends WhereParser {

    @Override
    protected WhereClause parseEQ(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            }
        }
        return new WhereClause(w.name() + " " + getWhereKeyWord(w.whereType()) + " ?", w.value1());
    }

    @Override
    protected WhereClause parseNE(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseLT(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseLTE(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseGT(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseGTE(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseLIKE(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseNOT_LIKE(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseLIKE_REGEX(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseNOT_LIKE_REGEX(Where where) {
        return parseEQ(where);
    }

    @Override
    public WhereClause parseIN(Where w) {
        return parseEQ(w);
    }

    @Override
    protected WhereClause parseNOT_IN(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseBETWEEN(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseNOT_BETWEEN(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseJSON_CONTAINS(Where where) {
        return parseEQ(where);
    }

    @Override
    protected WhereClause parseJSON_OVERLAPS(Where where) {
        return parseEQ(where);
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
