package cool.scx.sql.where;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
final class WhereOptionInfo {

    /**
     * 是否替换已有的相同名称的 WhereBody
     */
    private boolean replace = false;

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     */
    private boolean skipIfNull = false;

    /**
     * a
     */
    private boolean skipIfEmptyList = false;

    /**
     * 是否使用原始名称
     */
    private boolean useOriginalName = false;

    /**
     * 是否使用 json 类型的查询
     */
    private boolean useJsonExtract = false;

    /**
     * a
     *
     * @param whereOptions a
     */
    WhereOptionInfo(WhereOption... whereOptions) {
        for (var option : whereOptions) {
            switch (option) {
                case REPLACE -> this.replace = true;
                case SKIP_IF_NULL -> this.skipIfNull = true;
                case SKIP_IF_EMPTY_LIST -> this.skipIfEmptyList = true;
                case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                case USE_JSON_EXTRACT -> this.useJsonExtract = true;
            }
        }
    }

    /**
     * a
     *
     * @return a
     */
    boolean replace() {
        return replace;
    }

    /**
     * a
     *
     * @return a
     */
    boolean skipIfNull() {
        return skipIfNull;
    }

    /**
     * a
     *
     * @return a
     */
    boolean skipIfEmptyList() {
        return skipIfEmptyList;
    }

    /**
     * a
     *
     * @return a
     */
    boolean useOriginalName() {
        return useOriginalName;
    }

    /**
     * a
     *
     * @return a
     */
    boolean useJsonExtract() {
        return useJsonExtract;
    }

}
