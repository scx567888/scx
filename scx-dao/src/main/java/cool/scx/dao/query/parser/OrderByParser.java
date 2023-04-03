package cool.scx.dao.query.parser;

import cool.scx.dao.query.OrderBy;
import cool.scx.dao.query.OrderByBody;

public abstract class OrderByParser {

    public final String[] parseOrderBy(OrderBy orderBy) {
        return orderBy.orderByBodyList().stream().map(this::parseOrderByBody).toArray(String[]::new);
    }

    protected abstract String parseOrderByBody(OrderByBody body);

}
