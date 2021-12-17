package cool.scx.bo;

/**
 * 查询列过滤器
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
     * a
     *
     * @return a
     */
    public static SelectFilter ofIncluded() {
        return new SelectFilter(FilterMode.INCLUDED);
    }

    /**
     * a
     *
     * @return a
     */
    public static SelectFilter ofExcluded() {
        return new SelectFilter(FilterMode.EXCLUDED);
    }

}
