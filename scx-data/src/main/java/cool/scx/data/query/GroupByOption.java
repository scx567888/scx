package cool.scx.data.query;

/**
 * GroupByOption
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum GroupByOption {

    /**
     * 是否使用原始名称
     */
    USE_ORIGINAL_NAME,

    /**
     * 是否使用 json 类型的查询
     */
    USE_JSON_EXTRACT;

    public static final class Info {

        private boolean useOriginalName = false;

        private boolean useJsonExtract = false;

        public Info(GroupByOption... groupByOptions) {
            for (var option : groupByOptions) {
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
