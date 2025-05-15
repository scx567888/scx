package cool.scx.data.query.parser;

import cool.scx.data.query.*;

import java.util.ArrayList;

import static java.util.Collections.addAll;

/// WhereParser (只是一个帮助类, 实现可以选择性使用)
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
        // 我们无法确定用户输入的内容 为了安全起见 我们为这种自定义查询 两端拼接 ()
        // 保证在和其他子句拼接的时候不产生歧义
        return new WhereClause("(" + s + ")");
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

        //没有子句 压根不处理
        if (c == null) {
            return null;
        }

        var w = parse(c);
        if (w != null && !w.isEmpty()) {
            clause = w.whereClause();
            whereParams = w.params();
        }

        //因为其余解析方法已经保证了在可能出现歧义的子句两端拼接了括号, 所以这里添加 NOT 即可
        clause = getNotKeyWord(n) + " " + clause;
        return new WhereClause(clause, whereParams);
    }

    protected String getNotKeyWord(Not n) {
        return "NOT";
    }

    protected WhereClause parseWhere(Where body) {
        return switch (body.whereType()) {
            case EQ, NE, LT, LTE, GT, GTE,
                 LIKE_REGEX, NOT_LIKE_REGEX -> parseEqual(body);
            case LIKE, NOT_LIKE -> parseLike(body);
            case IN, NOT_IN -> parseIn(body);
            case BETWEEN, NOT_BETWEEN -> parseBetween(body);
            case JSON_CONTAINS, JSON_OVERLAPS -> parseJsonContains(body);
        };
    }

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

}
