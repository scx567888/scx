package cool.scx.data.query.parser;

import cool.scx.data.query.*;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class WhereParser {

    public WhereClause parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case WhereClause w -> parseWhereClause(w);
            case Logic l -> parseLogic(l);
            case Where w -> parseWhere(w);
            case Query q -> parseQuery(q);
            case Object[] o -> parseAll(o);
            default -> null;
        };
    }

    protected final WhereClause parseString(String s) {
        return new WhereClause(s);
    }

    protected WhereClause parseWhereClause(WhereClause w) {
        return w;
    }

    protected final WhereClause parseLogic(Logic l) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var c : l.clauses()) {
            var w = parse(c);
            if (w != null && !w.isEmpty()) {
                clauses.add(w.whereClause());
                addAll(whereParams, w.params());
            }
        }
        var clause = String.join(" " + getLogicKeyWord(l.logicType()) + " ", clauses);
        //只有 子句数量 大于 1 时, 我们才在两端拼接 括号
        if (clauses.size() > 1) {
            clause = "(" + clause + ")";
        }
        return new WhereClause(clause, whereParams.toArray());
    }

    protected String getLogicKeyWord(LogicType logicType) {
        return switch (logicType) {
            case OR -> "OR";
            case AND -> "AND";
        };
    }

    protected WhereClause parseWhere(Where body) {
        return switch (body.whereType()) {
            case IS_NULL, IS_NOT_NULL -> parseIsNull(body);
            case EQUAL, NOT_EQUAL,
                 LESS_THAN, LESS_THAN_OR_EQUAL,
                 GREATER_THAN, GREATER_THAN_OR_EQUAL,
                 LIKE_REGEX, NOT_LIKE_REGEX -> parseEqual(body);
            case LIKE, NOT_LIKE -> parseLike(body);
            case IN, NOT_IN -> parseIn(body);
            case BETWEEN, NOT_BETWEEN -> parseBetween(body);
            case JSON_CONTAINS, JSON_OVERLAPS -> parseJsonContains(body);
        };
    }

    protected abstract WhereClause parseIsNull(Where where);

    protected abstract WhereClause parseEqual(Where where);

    protected abstract WhereClause parseLike(Where where);

    protected abstract WhereClause parseIn(Where where);

    protected abstract WhereClause parseBetween(Where where);

    protected abstract WhereClause parseJsonContains(Where where);

    protected WhereClause parseQuery(Query query) {
        return parseAll(query.getWhere());
    }

    protected final WhereClause parseAll(Object[] objs) {
        var whereClause = new StringBuilder();
        var whereParams = new ArrayList<>();
        for (var obj : objs) {
            var w = parse(obj);
            if (w != null && !w.isEmpty()) {
                whereClause.append(w.whereClause());
                addAll(whereParams, w.params());
            }
        }
        return new WhereClause(whereClause.toString(), whereParams.toArray());
    }

    public String getWhereKeyWord(WhereType whereType) {
        return switch (whereType) {
            case IS_NULL -> "IS NULL";
            case IS_NOT_NULL -> "IS NOT NULL";
            case EQUAL -> "=";
            case NOT_EQUAL -> "<>";
            case LESS_THAN -> "<";
            case LESS_THAN_OR_EQUAL -> "<=";
            case GREATER_THAN -> ">";
            case GREATER_THAN_OR_EQUAL -> ">=";
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
