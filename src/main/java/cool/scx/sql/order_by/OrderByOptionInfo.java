package cool.scx.sql.order_by;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
final class OrderByOptionInfo {

    /**
     * 是否替换已有的相同名称的 WhereBody
     */
    private boolean replace = false;

    /**
     * 是否使用原始名称
     */
    private boolean useOriginalName = false;

    /**
     * 是否使用 json 类型的查询
     */
    private boolean useJsonExtract = false;

    public OrderByOptionInfo(OrderByOption... orderByOptions) {
        for (var option : orderByOptions) {
            switch (option) {
                case REPLACE -> this.replace = true;
                case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                case USE_JSON_EXTRACT -> this.useJsonExtract = true;
            }
        }
    }

    public boolean replace() {
        return replace;
    }

    public boolean useOriginalName() {
        return useOriginalName;
    }

    public boolean useJsonExtract() {
        return useJsonExtract;
    }

}
