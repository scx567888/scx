package cool.scx.data.query.parser;

import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderByBody;

public abstract class OrderByParser {

    public final String[] parseOrderBy(OrderBy orderBy) {
        return parseAll(orderBy.orderByBodyList());
    }

    public final String[] parseAll(OrderByBody[] objs) {
        var str = new String[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            str[i] = parse(objs[i]);
        }
        return str;
    }

    public String parse(OrderByBody obj) {
        return parseOrderByBody(obj);
    }

    protected abstract String parseOrderByBody(OrderByBody body);

}
