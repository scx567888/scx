package cool.scx.data.query.parser;

import cool.scx.data.query.*;

import java.util.ArrayList;
import java.util.List;

public abstract class WhereParser {

    public final WhereClauseAndWhereParams parseAll(Object[] objs) {
        var whereClause = new StringBuilder();
        var whereParams = new ArrayList<>();
        for (var obj : objs) {
            var w = parse(obj);
            if (w != null) {
                whereClause.append(w.whereClause());
                whereParams.addAll(List.of(w.whereParams()));
            }
        }
        return new WhereClauseAndWhereParams(whereClause.toString(), whereParams.toArray());
    }

    public WhereClauseAndWhereParams parse(Object obj) {
        if (obj instanceof String str) {
            return parseString(str);
        } else if (obj instanceof WhereBody whereBody) {
            return parseWhereBody(whereBody);
        } else if (obj instanceof Logic l) {
            return parseLogic(l);
        } else {
            return null;
        }
    }

    public final WhereClauseAndWhereParams parseString(String str) {
        return new WhereClauseAndWhereParams(str, new Object[]{});
    }

    public final WhereClauseAndWhereParams parseLogic(Logic l) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var c : l.clauses()) {
            var w = parse(c);
            if (w != null) {
                clauses.add(w.whereClause());
                whereParams.addAll(List.of(w.whereParams()));
            }
        }
        var clause = String.join(" " + getLogicKeyWord(l.type()) + " ", clauses);
        //只有 子句数量 大于 1 时, 我们才在两端拼接 括号
        if (clauses.size() > 1) {
            clause = "(" + clause + ")";
        }
        return new WhereClauseAndWhereParams(clause, whereParams.toArray());
    }

    public final WhereClauseAndWhereParams parseWhere(Where where) {
        return this.parseAll(where.whereBodyList());
    }

    public WhereClauseAndWhereParams parseWhereBody(WhereBody body) {
        var name = body.name();
        var whereType = body.whereType();
        var value1 = body.value1();
        var value2 = body.value2();
        var info = body.info();
        return switch (whereType) {
            case IS_NULL, IS_NOT_NULL -> parseIsNull(name, whereType, value1, value2, info);
            case EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LIKE_REGEX, NOT_LIKE_REGEX ->
                    parseEqual(name, whereType, value1, value2, info);
            case LIKE, NOT_LIKE -> parseLike(name, whereType, value1, value2, info);
            case IN, NOT_IN -> parseIn(name, whereType, value1, value2, info);
            case BETWEEN, NOT_BETWEEN -> parseBetween(name, whereType, value1, value2, info);
            case JSON_CONTAINS -> parseJsonContains(name, whereType, value1, value2, info);
        };
    }

    public abstract WhereClauseAndWhereParams parseJsonContains(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

    public abstract WhereClauseAndWhereParams parseBetween(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

    public abstract WhereClauseAndWhereParams parseIn(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

    public abstract WhereClauseAndWhereParams parseLike(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

    public abstract WhereClauseAndWhereParams parseEqual(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

    public abstract WhereClauseAndWhereParams parseIsNull(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

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
