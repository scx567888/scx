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
    private boolean useOriginalName = false;

    /**
     * 是否使用 json 类型的查询
     */
    private boolean useJsonExtract = false;

    /**
     * <p>Constructor for GroupByOptionInfo.</p>
     *
     * @param groupByOptions a {@link cool.scx.sql.group_by.GroupByOption} object
     */
    public GroupByOptionInfo(GroupByOption... groupByOptions) {
        for (var option : groupByOptions) {
            switch (option) {
                case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                case USE_JSON_EXTRACT -> this.useJsonExtract = true;
            }
        }
    }

    /**
     * <p>useOriginalName.</p>
     *
     * @return a boolean
     */
    public boolean useOriginalName() {
        return useOriginalName;
    }

    /**
     * <p>useJsonExtract.</p>
     *
     * @return a boolean
     */
    public boolean useJsonExtract() {
        return useJsonExtract;
    }

}
