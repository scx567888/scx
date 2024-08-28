package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderBySet;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class OrderByParser {

    public String[] parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case OrderBy o -> parseOrderBy(o);
            case OrderBySet s -> parseOrderBySet(s);
            case Query q -> parseQuery(q);
            default -> null;
        };
    }

    protected String[] parseString(String s) {
        return new String[]{s};
    }

    protected abstract String[] parseOrderBy(OrderBy body);

    protected String[] parseOrderBySet(OrderBySet body) {
        return parseAll(body.clauses());
    }

    protected String[] parseQuery(Query body) {
        return parseAll(body.getOrderBy());
    }

    protected final String[] parseAll(Object[] objs) {
        var list = new ArrayList<String>();
        for (var obj : objs) {
            var s = parse(obj);
            addAll(list, s);
        }
        return list.toArray(String[]::new);
    }

}
