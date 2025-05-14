package cool.scx.data.query.parser;

import cool.scx.data.query.*;

import java.util.ArrayList;

import static java.util.Collections.addAll;

/// WhereParser
///
/// @author scx567888
/// @version 0.0.1
public abstract class WhereParser {

    public WhereClause parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case WhereClause w -> parseWhereClause(w);
            case Junction j -> parseJunction(j);
            case Not n -> parseNot(n);
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

    protected final WhereClause parseJunction(Junction j) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var c : j.clauses()) {
            var w = parse(c);
            if (w != null && !w.isEmpty()) {
                clauses.add(w.whereClause());
                addAll(whereParams, w.params());
            }
        }
        var clause = String.join(" " + getJunctionKeyWord(j) + " ", clauses);
        //只有 子句数量 大于 1 时, 我们才在两端拼接 括号
        if (clauses.size() > 1) {
            clause = "(" + clause + ")";
        }
        return new WhereClause(clause, whereParams.toArray());
    }

    protected String getJunctionKeyWord(Junction junction) {
        return switch (junction) {
            case Or _ -> "OR";
            case And _ -> "AND";
        };
    }

    protected WhereClause parseNot(Not n) {
        String clause = null;
        Object[] whereParams = null;

        var c = n.clause();

        var w = parse(c);
        if (w != null && !w.isEmpty()) {
            clause = w.whereClause();
            whereParams = w.params();
        }

        //因为 and 和 or 已经保证了在两端拼接 括号, 所以 这里不用拼接 括号 
        // todo 用户写的字符串表达式怎么办 加括号防御 ? 同理这样是不是表示 and 和 or 也需要为每个子句加括号 ? 还是统一在 字符串上加括号 ? 
        clause = getNotKeyWord(n) + " " + clause;
        return new WhereClause(clause, whereParams);
    }

    protected String getNotKeyWord(Not n) {
        return "NOT";
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
