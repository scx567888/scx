package cool.scx.sql.pagination;

/**
 * 分页参数
 *
 * @author scx567888
 * @version 1.2.0
 */
public final class Pagination {

    /**
     * 当前页 页码 默认为空 即不设置页码
     */
    private Integer currentPage = null;

    /**
     * 每页数量分页 每页数量 默认为空 即不设置分页内容
     */
    private Integer pageSize = null;

    /**
     * <p>Constructor for Pagination.</p>
     */
    public Pagination() {

    }

    /**
     * 设置分页参数
     *
     * @param page 分页页码 注意从 0 开始
     * @param size 每页数量
     * @return p
     */
    public Pagination set(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("分页参数错误 : page (分页页码) 不能为空或小于 0 !!!");
        }
        if (size == null || size < 0) {
            throw new IllegalArgumentException("分页参数错误 : size (每页数量) 不能为空或小于 0 !!!");
        }
        this.currentPage = page;
        this.pageSize = size;
        return this;
    }

    /**
     * 设置每页数量
     *
     * @param size s
     * @return s
     */
    public Pagination set(Integer size) {
        return set(0, size);
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    public Integer offset() {
        return pageSize != null && currentPage != null ? pageSize * currentPage : null;
    }

    /**
     * a
     *
     * @return a
     */
    public Pagination clear() {
        this.currentPage = null;
        this.pageSize = null;
        return this;
    }

    /**
     * 获取分页大小 (这里分页大小即等于行数 所以直接返回 pageSize)
     *
     * @return rowCount
     */
    public Integer rowCount() {
        return pageSize;
    }

}
