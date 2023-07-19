package cool.scx.data.query;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum FieldFilterOption {

    /**
     * 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除 默认启用
     */
    EXCLUDE_IF_FIELD_VALUE_IS_NULL;

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
        private boolean excludeIfFieldValueIsNull = false;

        /**
         * <p>Constructor for GroupByOptionInfo.</p>
         *
         * @param groupByOptions a {@link FieldFilterOption} object
         */
        public Info(FieldFilterOption... groupByOptions) {
            for (var option : groupByOptions) {
                switch (option) {
                    case EXCLUDE_IF_FIELD_VALUE_IS_NULL -> this.excludeIfFieldValueIsNull = true;
                }
            }
        }

        /**
         * <p>useOriginalName.</p>
         *
         * @return a boolean
         */
        public boolean excludeIfFieldValueIsNull() {
            return excludeIfFieldValueIsNull;
        }

    }

}
