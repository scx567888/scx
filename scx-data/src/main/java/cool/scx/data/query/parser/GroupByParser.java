package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.GroupBy;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public abstract class GroupByParser {

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
            case GroupBy g -> new String[]{parseGroupBy(g)};
            case Query q -> parseAll(q.getGroupBy());
            default -> null;
        };
    }

    private String parseString(String str) {
        return str;
    }

    public abstract String parseGroupBy(GroupBy body);

}
