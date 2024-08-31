package cool.scx.data.query.parser;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.Query;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class GroupByParser {

    public String[] parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case GroupBy g -> parseGroupBy(g);
            case Query q -> parseQuery(q);
            case Object[] o -> parseAll(o);
            default -> null;
        };
    }

    protected String[] parseString(String s) {
        return new String[]{s};
    }

    protected abstract String[] parseGroupBy(GroupBy g);

    protected String[] parseQuery(Query q) {
        return parseAll(q.getGroupBy());
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
