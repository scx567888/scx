package cool.scx.base;

/**
 * 查询列过滤器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class SelectFilter extends AbstractFilter<SelectFilter> {

    /**
     * a
     *
     * @param filterMode a
     */
    private SelectFilter(FilterMode filterMode) {
        super(filterMode);
    }

    /**
     * 白名单模式
     *
     * @return a
     */
    public static SelectFilter ofIncluded() {
        return new SelectFilter(FilterMode.INCLUDED);
    }

    /**
     * 黑名单模式
     *
     * @return a
     */
    public static SelectFilter ofExcluded() {
        return new SelectFilter(FilterMode.EXCLUDED);
    }

    /**
     * 白名单模式
     *
     * @return a
     */
    public static SelectFilter ofIncluded(String... fieldNames) {
        return ofIncluded().addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @return a
     */
    public static SelectFilter ofExcluded(String... fieldNames) {
        return ofExcluded().addExcluded(fieldNames);
    }

}
