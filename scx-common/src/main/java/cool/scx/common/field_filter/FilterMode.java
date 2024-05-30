package cool.scx.common.field_filter;

/**
 * 过滤模式
 */
public enum FilterMode {

    /**
     * 包含模式
     */
    INCLUDED,

    /**
     * 排除模式
     */
    EXCLUDED;

    public static FilterMode of(String filterModeStr) {
        return valueOf(filterModeStr.trim().toUpperCase());
    }

}
