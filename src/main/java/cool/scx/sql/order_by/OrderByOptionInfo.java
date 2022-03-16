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
    private final boolean replace;

    /**
     * 是否使用原始名称
     */
    private final boolean useOriginalName;

    /**
     * 是否使用 json 类型的查询
     */
    private final boolean useJsonExtract;

    public OrderByOptionInfo(OrderByOption... orderByOptions) {
        var _replace = false;// 是否替换已有的相同名称的 WhereBody
        var _useOriginalName = false;// 是否使用原始名称
        var _useJsonExtract = false;// 是否使用 json 类型的查询
        for (var option : orderByOptions) {
            switch (option) {
                case REPLACE -> _replace = true;
                case USE_ORIGINAL_NAME -> _useOriginalName = true;
                case USE_JSON_EXTRACT -> _useJsonExtract = true;
            }
        }
        this.replace = _replace;
        this.useOriginalName = _useOriginalName;
        this.useJsonExtract = _useJsonExtract;
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
