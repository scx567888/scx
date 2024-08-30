package cool.scx.common.field_filter;

public class FieldFilterBuilder {

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(String... fieldNames) {
        return new IncludedFieldFilter().addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(String... fieldNames) {
        return new ExcludedFieldFilter().addExcluded(fieldNames);
    }

}
