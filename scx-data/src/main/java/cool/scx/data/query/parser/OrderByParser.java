package cool.scx.data.query.parser;

import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Query;

import java.util.ArrayList;

import static java.util.Collections.addAll;

/// OrderByParser
///
/// @author scx567888
/// @version 0.0.1
public abstract class OrderByParser {

    public String[] parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case OrderBy o -> parseOrderBy(o);
            case Query q -> parseQuery(q);
            case Object[] o -> parseAll(o);
            default -> null;
        };
    }

    protected String[] parseString(String s) {
        return new String[]{s};
    }

    protected abstract String[] parseOrderBy(OrderBy o);

    protected String[] parseQuery(Query q) {
        return parseAll(q.getOrderBy());
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
