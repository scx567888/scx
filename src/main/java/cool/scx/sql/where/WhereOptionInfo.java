package cool.scx.sql.where;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
final class WhereOptionInfo {

    /**
     * a
     */
    private boolean ignoreExceptionIfNull;

    /**
     * a
     */
    private boolean ignoreExceptionIfEmptyList;

    /**
     * a
     */
    private boolean skipIfNullInList;

    /**
     * a
     */
    private boolean ignoreExceptionIfNullInList;

    /**
     * 是否替换已有的相同名称的 WhereBody
     */
    private boolean replace = false;

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     */
    private boolean skipIfNull = false;

    /**
     * 在 in 或 not in 中 如果参数列表为空则跳过添加而不是报错
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

    public WhereOptionInfo(WhereOption... whereOptions) {
        for (var option : whereOptions) {
            switch (option) {
                case REPLACE -> this.replace = true;
                case SKIP_IF_NULL -> this.skipIfNull = true;
                case SKIP_IF_EMPTY_LIST -> this.skipIfEmptyList = true;
                case SKIP_IF_NULL_IN_LIST -> this.skipIfNullInList = true;
                case IGNORE_EXCEPTION_IF_NULL -> this.ignoreExceptionIfNull = true;
                case IGNORE_EXCEPTION_IF_EMPTY_LIST -> this.ignoreExceptionIfEmptyList = true;
                case IGNORE_EXCEPTION_IF_NULL_IN_LIST -> this.ignoreExceptionIfNullInList = true;
                case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                case USE_JSON_EXTRACT -> this.useJsonExtract = true;
            }
        }
    }

    public boolean replace() {
        return replace;
    }

    public boolean skipIfNull() {
        return skipIfNull;
    }

    public boolean useOriginalName() {
        return useOriginalName;
    }

    public boolean useJsonExtract() {
        return useJsonExtract;
    }

    public boolean skipIfEmptyList() {
        return skipIfEmptyList;
    }

    public boolean ignoreExceptionIfNull() {
        return ignoreExceptionIfNull;
    }

    public boolean ignoreExceptionIfEmptyList() {
        return ignoreExceptionIfEmptyList;
    }

    public boolean skipIfNullInList() {
        return skipIfNullInList;
    }

    public boolean ignoreExceptionIfNullInList() {
        return ignoreExceptionIfNullInList;
    }

}
