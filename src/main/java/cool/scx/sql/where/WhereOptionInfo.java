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
    private final boolean replace;

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     */
    private final boolean skipIfNull;

    /**
     * 是否使用原始名称
     */
    private final boolean useOriginalName;

    /**
     * 是否使用 json 类型的查询
     */
    private final boolean useJsonExtract;

    public WhereOptionInfo(WhereOption... whereOptions) {
        var _replace = false;// 是否替换已有的相同名称的 WhereBody
        var _skipIfNull = false;//true : 如果参数为null 则跳过 , false 抛出参数不能为空异常
        var _useOriginalName = false;// 是否使用原始名称
        var _useJsonExtract = false;// 是否使用 json 类型的查询
        for (var option : whereOptions) {
            switch (option) {
                case REPLACE -> _replace = true;
                case SKIP_IF_NULL -> _skipIfNull = true;
                case USE_ORIGINAL_NAME -> _useOriginalName = true;
                case USE_JSON_EXTRACT -> _useJsonExtract = true;
            }
        }
        this.replace = _replace;
        this.skipIfNull = _skipIfNull;
        this.useOriginalName = _useOriginalName;
        this.useJsonExtract = _useJsonExtract;
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

}
