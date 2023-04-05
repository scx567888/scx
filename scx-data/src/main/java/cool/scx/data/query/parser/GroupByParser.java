package cool.scx.data.query.parser;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;

public abstract class GroupByParser {

    public final String[] parseGroupBy(GroupBy groupBy) {
        //此处去重
        return groupBy.groupByBodyList().stream().map(this::parseGroupByBody).distinct().toArray(String[]::new);
    }

    public abstract String parseGroupByBody(GroupByBody body);

}
