package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderByBody;
import cool.scx.data.query.OrderByBodySet;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class OrderByParser {

    public final String[] parseOrderBy(OrderBy orderBy) {
        return parseAll(orderBy.clauses());
    }

    public final String[] parseAll(Object[] objs) {
        var list = new ArrayList<String>();
        for (var obj : objs) {
            var s = parse(obj);
            addAll(list, s);
        }
        return list.toArray(String[]::new);
    }

    public String[] parse(Object obj) {
        if (obj instanceof OrderByBody o) {
            return new String[]{parseOrderByBody(o)};
        } else if (obj instanceof String s) {
            return new String[]{s};
        } else if (obj instanceof OrderByBodySet s) {
            return parseAll(s.clauses());
        } else if (obj instanceof OrderBy s) {
            return parseAll(s.clauses());
        } else if (obj instanceof Query q) {
            return parseAll(q.getOrderBy().clauses());
        } else {
            return null;
        }
    }

    protected abstract String parseOrderByBody(OrderByBody body);

}
