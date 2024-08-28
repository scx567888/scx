package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderBySet;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class OrderByParser {

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
            case String s -> new String[]{parseString(s)};
            case OrderBy o -> new String[]{parseOrderBy(o)};
            case OrderBySet s -> parseAll(s.clauses());
            case Query q -> parseAll(q.getOrderBy());
            default -> null;
        };
    }

    private String parseString(String str) {
        return str;
    }

    public abstract String parseOrderBy(OrderBy body);

}
