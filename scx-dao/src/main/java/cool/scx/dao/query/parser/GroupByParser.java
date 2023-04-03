package cool.scx.dao.query.parser;

import cool.scx.dao.query.GroupBy;
import cool.scx.dao.query.GroupByBody;

public abstract class GroupByParser {

    public final String[] parseGroupBy(GroupBy groupBy) {
        //此处去重
        return groupBy.groupByBodyList().stream().map(this::parseGroupByBody).distinct().toArray(String[]::new);
    }

    public abstract String parseGroupByBody(GroupByBody body);

}
