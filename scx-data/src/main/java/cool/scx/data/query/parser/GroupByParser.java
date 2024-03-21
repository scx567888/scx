package cool.scx.data.query.parser;

import cool.scx.data.Query;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.addAll;

public abstract class GroupByParser {

    public final String[] parseGroupBy(GroupBy groupBy) {
        var all = parseAll(groupBy.clauses());
        //此处去重
        return Arrays.stream(all).distinct().toArray(String[]::new);
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
            case String str -> new String[]{parseString(str)};
            case GroupByBody groupByBody -> new String[]{parseGroupByBody(groupByBody)};
            case Query q -> parseAll(q.getGroupBy().clauses());
            case null, default -> null;
        };
    }

    private String parseString(String str) {
        return str;
    }

    public abstract String parseGroupByBody(GroupByBody body);

}
