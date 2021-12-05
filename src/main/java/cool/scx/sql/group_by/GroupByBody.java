package cool.scx.sql.group_by;

import cool.scx.util.CaseUtils;

final class GroupByBody {

    private final String name;

    private final String groupByColumn;

    /**
     *
     */
    GroupByBody(String _name, boolean useOriginalName) {
        this.name = _name.trim();
        this.groupByColumn = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
    }

    String name() {
        return name;
    }

    String groupByColumn() {
        return groupByColumn;
    }

}
