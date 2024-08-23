package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.*;
import cool.scx.data.query.WhereOption.Info;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class WhereParser {

    public final WhereClause parseAll(Object[] objs) {
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

    public WhereClause parse(Object obj) {
        return switch (obj) {
            case String str -> parseString(str);
            case WhereBody whereBody -> parseWhereBody(whereBody);
            case Logic l -> parseLogic(l);
            case WhereClause w -> w;
            case Where w -> parseWhere(w);
            case Query q -> parseWhere(q.getWhere());
            case null, default -> null;
        };
    }

    public final WhereClause parseString(String str) {
        return new WhereClause(str);
    }

    public final WhereClause parseLogic(Logic l) {
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

    public final WhereClause parseWhere(Where where) {
        return this.parseAll(where.clauses());
    }

    public WhereClause parseWhereBody(WhereBody body) {
        var name = body.name();
        var whereType = body.whereType();
        var value1 = body.value1();
        var value2 = body.value2();
        var info = body.info();
        return switch (whereType) {
            case IS_NULL, IS_NOT_NULL -> parseIsNull(name, whereType, value1, value2, info);
            case EQUAL, NOT_EQUAL,
                 LESS_THAN, LESS_THAN_OR_EQUAL,
                 GREATER_THAN, GREATER_THAN_OR_EQUAL,
                 LIKE_REGEX, NOT_LIKE_REGEX -> parseEqual(name, whereType, value1, value2, info);
            case LIKE, NOT_LIKE -> parseLike(name, whereType, value1, value2, info);
            case IN, NOT_IN -> parseIn(name, whereType, value1, value2, info);
            case BETWEEN, NOT_BETWEEN -> parseBetween(name, whereType, value1, value2, info);
            case JSON_CONTAINS -> parseJsonContains(name, whereType, value1, value2, info);
        };
    }

    public abstract WhereClause parseJsonContains(String name, WhereType whereType, Object value1, Object value2, Info info);

    public abstract WhereClause parseBetween(String name, WhereType whereType, Object value1, Object value2, Info info);

    public abstract WhereClause parseIn(String name, WhereType whereType, Object value1, Object value2, Info info);

    public abstract WhereClause parseLike(String name, WhereType whereType, Object value1, Object value2, Info info);

    public abstract WhereClause parseEqual(String name, WhereType whereType, Object value1, Object value2, Info info);

    public abstract WhereClause parseIsNull(String name, WhereType whereType, Object value1, Object value2, Info info);

    public String getLogicKeyWord(LogicType logicType) {
        return switch (logicType) {
            case OR -> "OR";
            case AND -> "AND";
        };
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
        };
    }

}
