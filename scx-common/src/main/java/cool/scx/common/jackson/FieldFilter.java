package cool.scx.common.jackson;

import java.util.Set;

import static cool.scx.common.jackson.FieldFilter.FilterMode.EXCLUDED;
import static cool.scx.common.jackson.FieldFilter.FilterMode.INCLUDED;

public class FieldFilter {

    private final FilterMode filterMode;
    private final String[] fieldNames;

    private FieldFilter(FilterMode filterMode, String... fieldNames) {
        this.filterMode = filterMode;
        this.fieldNames = Set.of(fieldNames).toArray(String[]::new);
    }

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(String... fieldNames) {
        return new FieldFilter(INCLUDED, fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(String... fieldNames) {
        return new FieldFilter(EXCLUDED, fieldNames);
    }

    FilterMode getFilterMode() {
        return filterMode;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    enum FilterMode {
        INCLUDED,
        EXCLUDED
    }

}
