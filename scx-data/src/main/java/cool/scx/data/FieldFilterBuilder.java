package cool.scx.data;

import cool.scx.data.field_filter.ExcludedFieldFilter;
import cool.scx.data.field_filter.IncludedFieldFilter;

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
