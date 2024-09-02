package cool.scx.common.field_filter;

import static cool.scx.common.field_filter.FilterMode.EXCLUDED;
import static cool.scx.common.field_filter.FilterMode.INCLUDED;

public class FieldFilterBuilder {

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(String... fieldNames) {
        return new FieldFilterImpl(INCLUDED).addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(String... fieldNames) {
        return new FieldFilterImpl(EXCLUDED).addExcluded(fieldNames);
    }

}
