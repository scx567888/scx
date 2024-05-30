package cool.scx.data.query;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum WhereOption {

    /**
     * 替换同名的 where 参数
     */
    REPLACE,

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     * <br>
     * 这里虽然叫做 SKIP_IF_NULL 但实际上表示的有效参数数量是不是和所接受的参数数量一致
     * <br>
     * 只是为了简化书写
     */
    SKIP_IF_NULL,

    /**
     * 在 in 或 not in 中 如果有效的参数条目 (指去除 null 后的) 为空 则跳过添加而不是报错
     * <br>
     * 和 {@link  WhereOption#SKIP_IF_NULL} 相同 是为了简化书写 其实际意义为参数中去除非法数值(为 null)后的列表长度是否为 0
     */
    SKIP_IF_EMPTY_LIST,

    /**
     * 使用原始名称 (不进行转换)
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 json 查询
     * <br>
     * 注意和 {@link WhereType#JSON_CONTAINS} 一起使用时无效 因为 {@link WhereType#JSON_CONTAINS} 自己有针对 Json 的特殊实现
     */
    USE_JSON_EXTRACT,

    /**
     * 包含 null 值 只适用于 JSON_CONTAINS
     */
    JSON_CONTAINS_INCLUDE_NULL_VALUE,

    /**
     * 注意只适用于 JSON_CONTAINS
     * JSON_CONTAINS 默认会将值转换为 JSON 并去除为 value 为 null 的 字段
     * 使用 原始值 时会将值 直接传递到 SQL 语句
     * 若值为 实体类 则会转换为 JSON 不过 和默认情况相比, 转换的 JSON 会包含 value 为 null 的字段
     */
    USE_ORIGINAL_VALUE;

    /**
     * a
     *
     * @author scx567888
     * @version 0.0.1
     */
    public static final class Info {

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
         * 注意只适用于 JSON_CONTAINS
         * 是否使用 原始值
         */
        private boolean useOriginalValue = false;

        /**
         * a
         *
         * @param whereOptions a
         */
        Info(WhereOption... whereOptions) {
            for (var option : whereOptions) {
                switch (option) {
                    case REPLACE -> this.replace = true;
                    case SKIP_IF_NULL -> this.skipIfNull = true;
                    case SKIP_IF_EMPTY_LIST -> this.skipIfEmptyList = true;
                    case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                    case USE_JSON_EXTRACT -> this.useJsonExtract = true;
                    case USE_ORIGINAL_VALUE -> this.useOriginalValue = true;
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
        public boolean useOriginalName() {
            return useOriginalName;
        }

        /**
         * a
         *
         * @return a
         */
        public boolean useJsonExtract() {
            return useJsonExtract;
        }

        public boolean useOriginalValue() {
            return useOriginalValue;
        }

    }

}
