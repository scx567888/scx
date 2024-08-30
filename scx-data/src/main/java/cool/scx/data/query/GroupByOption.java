package cool.scx.data.query;

/**
 * GroupByOption
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
     * 使用 JSON 查询
     */
    USE_JSON_EXTRACT;

    public static final class Info {

        private boolean useOriginalName = false;
        private boolean useJsonExtract = false;

        Info(GroupByOption... groupByOptions) {
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
