package cool.scx.orm.query.parser;

import cool.scx.orm.jdbc.sql.SQL;
import cool.scx.orm.query.AND;
import cool.scx.orm.query.Logic;
import cool.scx.orm.query.Where;
import cool.scx.orm.query.WhereBody;

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

    public final WhereClauseAndWhereParams parse(Object obj) {
        if (obj instanceof String str) {
            return parseString(str);
        } else if (obj instanceof WhereBody whereBody) {
            return parseWhereBody(whereBody);
        } else if (obj instanceof SQL s) {
            return parseSQL(s);
        } else if (obj instanceof Logic l) {
            return parseLogic(l);
        } else {
            return null;
        }
    }

    public final WhereClauseAndWhereParams parseString(String str) {
        return new WhereClauseAndWhereParams(str, new Object[]{});
    }

    public abstract WhereClauseAndWhereParams parseWhereBody(WhereBody body);

    public final WhereClauseAndWhereParams parseSQL(SQL sql) {
        return new WhereClauseAndWhereParams("(" + sql.sql() + ")", sql.params());
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
        var clause = String.join(" " + l.keyWord() + " ", clauses);
        //只有 子句数量 大于 1 时, 我们才在两端拼接 括号
        if (clauses.size() > 1) {
            clause = "(" + clause + ")";
        }
        return new WhereClauseAndWhereParams(clause, whereParams.toArray());
    }

    public final WhereClauseAndWhereParams parseWhere(Where where) {
        //先处理 whereBodyList 默认我们将其全部用 AND 进行拼接
        var w1 = this.parse(new AND(where.whereBodyList().toArray()));
        var w2 = this.parseAll(where.whereSQL());
        var whereClause = w1.whereClause() + w2.whereClause();
        var whereParams = new ArrayList<>();
        whereParams.addAll(List.of(w1.whereParams()));
        whereParams.addAll(List.of(w2.whereParams()));
        return new WhereClauseAndWhereParams(whereClause, whereParams.toArray());
    }

}
