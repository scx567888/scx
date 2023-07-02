package cool.scx.data.query;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum GroupByOption {

    /**
     * 使用原始名称
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 json 表达式
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
         * @param groupByOptions a {@link GroupByOption} object
         */
        public Info(GroupByOption... groupByOptions) {
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

}
