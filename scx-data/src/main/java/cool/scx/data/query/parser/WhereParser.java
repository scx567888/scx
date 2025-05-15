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
            case null -> new WhereClause(null);
            default -> throw new IllegalArgumentException("Unsupported object type: " + obj.getClass());
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

        if (clauses.isEmpty()) {
            return new WhereClause(null);
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

        var w = parse(n.clause());

        if (w != null && !w.isEmpty()) {
            //因为其余解析方法已经保证了在可能出现歧义的子句两端拼接了括号, 所以这里直接添加 NOT 即可
            return new WhereClause(getNotKeyWord(n) + " " + w.whereClause(), w.params());
        } else {
            return new WhereClause(null);
        }

    }

    protected String getNotKeyWord(Not n) {
        return "NOT";
    }

    protected WhereClause parseQuery(Query query) {
        return parse(query.getWhere());
    }

    protected final WhereClause parseAll(Object[] objs) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var obj : objs) {
            var w = parse(obj);
            if (w != null && !w.isEmpty()) {
                clauses.add(w.whereClause());
                addAll(whereParams, w.params());
            }
        }

        if (clauses.isEmpty()) {
            return new WhereClause(null);
        }

        return new WhereClause(String.join("", clauses), whereParams.toArray());
    }

    protected WhereClause parseWhere(Where body) {
        return switch (body.whereType()) {
            case EQ -> parseEQ(body);
            case NE -> parseNE(body);
            case LT -> parseLT(body);
            case LTE -> parseLTE(body);
            case GT -> parseGT(body);
            case GTE -> parseGTE(body);
            case LIKE -> parseLIKE(body);
            case NOT_LIKE -> parseNOT_LIKE(body);
            case LIKE_REGEX -> parseLIKE_REGEX(body);
            case NOT_LIKE_REGEX -> parseNOT_LIKE_REGEX(body);
            case IN -> parseIN(body);
            case NOT_IN -> parseNOT_IN(body);
            case BETWEEN -> parseBETWEEN(body);
            case NOT_BETWEEN -> parseNOT_BETWEEN(body);
            case JSON_CONTAINS -> parseJSON_CONTAINS(body);
            case JSON_OVERLAPS -> parseJSON_OVERLAPS(body);
        };
    }

    protected abstract WhereClause parseEQ(Where where);

    protected abstract WhereClause parseNE(Where where);

    protected abstract WhereClause parseLT(Where where);

    protected abstract WhereClause parseLTE(Where where);

    protected abstract WhereClause parseGT(Where where);

    protected abstract WhereClause parseGTE(Where where);

    protected abstract WhereClause parseLIKE(Where where);

    protected abstract WhereClause parseNOT_LIKE(Where where);

    protected abstract WhereClause parseLIKE_REGEX(Where where);

    protected abstract WhereClause parseNOT_LIKE_REGEX(Where where);

    protected abstract WhereClause parseIN(Where where);

    protected abstract WhereClause parseNOT_IN(Where where);

    protected abstract WhereClause parseBETWEEN(Where where);

    protected abstract WhereClause parseNOT_BETWEEN(Where where);

    protected abstract WhereClause parseJSON_CONTAINS(Where where);

    protected abstract WhereClause parseJSON_OVERLAPS(Where where);

}
