package cool.scx.data.query;

/**
 * OrderByOption
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum OrderByOption {

    /**
     * 替换现有
     */
    REPLACE,

    /**
     * 使用原始名称
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 JSON 查询
     */
    USE_JSON_EXTRACT;

    public static final class Info {

        private boolean replace = false;
        private boolean useOriginalName = false;
        private boolean useJsonExtract = false;

        Info(OrderByOption... orderByOptions) {
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
