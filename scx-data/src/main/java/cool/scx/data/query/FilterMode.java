package cool.scx.data.query;

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

    /**
     * a
     *
     * @param filterModeStr a
     * @return a
     */
    public static FilterMode of(String filterModeStr) {
        return valueOf(filterModeStr.trim().toUpperCase());
    }

}
