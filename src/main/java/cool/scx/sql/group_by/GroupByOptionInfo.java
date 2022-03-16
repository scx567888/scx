package cool.scx.sql.group_by;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
final class GroupByOptionInfo {

    /**
     * 是否使用原始名称
     */
    private final boolean useOriginalName;

    /**
     * 是否使用 json 类型的查询
     */
    private final boolean useJsonExtract;

    public GroupByOptionInfo(GroupByOption... groupByOptions) {
        var _useOriginalName = false;// 是否使用原始名称
        var _useJsonExtract = false;// 是否使用 json 类型的查询
        for (var option : groupByOptions) {
            switch (option) {
                case USE_ORIGINAL_NAME -> _useOriginalName = true;
                case USE_JSON_EXTRACT -> _useJsonExtract = true;
            }
        }
        this.useOriginalName = _useOriginalName;
        this.useJsonExtract = _useJsonExtract;
    }

    public boolean useOriginalName() {
        return useOriginalName;
    }

    public boolean useJsonExtract() {
        return useJsonExtract;
    }

}
