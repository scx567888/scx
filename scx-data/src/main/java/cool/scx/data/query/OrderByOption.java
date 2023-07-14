package cool.scx.data.query;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum OrderByOption {

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
                    case USE_ORIGINAL_NAME -> this.useOriginalName = true;
                    case USE_JSON_EXTRACT -> this.useJsonExtract = true;
                }
            }
        }

        public boolean useOriginalName() {
            return useOriginalName;
        }

        public boolean useJsonExtract() {
            return useJsonExtract;
        }

    }

}
