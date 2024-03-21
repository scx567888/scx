package cool.scx.data.query;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum OrderByOption {

    /**
     * 替换现有同名字段
     */
    REPLACE,

    /**
     * 使用原始名称
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 json 查询
     */
    USE_JSON_EXTRACT;

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
         * 是否使用原始名称
         */
        private boolean useOriginalName = false;

        /**
         * 是否使用 json 类型的查询
         */
        private boolean useJsonExtract = false;

        public Info(OrderByOption... orderByOptions) {
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

}
