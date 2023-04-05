package cool.scx.data.query.parser;

import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderByBody;

public abstract class OrderByParser {

    public final String[] parseOrderBy(OrderBy orderBy) {
        return orderBy.orderByBodyList().stream().map(this::parseOrderByBody).toArray(String[]::new);
    }

    protected abstract String parseOrderByBody(OrderByBody body);

}
