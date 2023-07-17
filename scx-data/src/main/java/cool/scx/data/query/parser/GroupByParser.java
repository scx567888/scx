package cool.scx.data.query.parser;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;

import java.util.Arrays;

public abstract class GroupByParser {

    public final String[] parseGroupBy(GroupBy groupBy) {
        var all = parseAll(groupBy.clauses());
        //此处去重
        return Arrays.stream(all).distinct().toArray(String[]::new);
    }

    public final String[] parseAll(Object[] objs) {
        var str = new String[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            str[i] = parse(objs[i]);
        }
        return str;
    }

    public String parse(Object obj) {
        if (obj instanceof String str) {
            return parseString(str);
        } else if (obj instanceof GroupByBody groupByBody) {
            return parseGroupByBody(groupByBody);
        } else {
            return null;
        }
    }

    private String parseString(String str) {
        return str;
    }

    public abstract String parseGroupByBody(GroupByBody body);

}
