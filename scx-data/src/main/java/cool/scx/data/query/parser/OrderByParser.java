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
        return switch (obj) {
            case OrderByBody o -> new String[]{parseOrderByBody(o)};
            case String s -> new String[]{s};
            case OrderByBodySet s -> parseAll(s.clauses());
            case OrderBy s -> parseAll(s.clauses());
            case Query q -> parseAll(q.getOrderBy().clauses());
            case null, default -> null;
        };
    }

    protected abstract String parseOrderByBody(OrderByBody body);

}
