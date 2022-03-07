package cool.scx.sql.pagination;

/**
 * 分页参数
 *
 * @author scx567888
 * @version 1.2.0
 */
public final class Pagination {

    /**
     * 分页 页码 默认为空 即不设置分页页码
     */
    private Integer page = 0;

    /**
     * 分页 每页数量 默认为空 即不设置分页内容
     */
    private Integer size = null;

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
            throw new IllegalArgumentException("分页参数错误 : page 不能为空或小于 0 !!!");
        }
        if (size == null || size < 0) {
            throw new IllegalArgumentException("分页参数错误 : size 不能为空或小于 0 !!!");
        }
        this.page = page;
        this.size = size;
        return this;
    }

    /**
     * 设置每页数量
     *
     * @param size s
     * @return s
     */
    public Pagination set(Integer size) {
        if (size < 0) {
            throw new IllegalArgumentException("分页参数错误 : size 不能为空或小于 0 !!!");
        }
        this.size = size;
        return this;
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    public Integer offset() {
        return size != null ? page * size : null;
    }

    /**
     * 获取分页大小
     *
     * @return a int
     */
    public Integer size() {
        return size;
    }

    /**
     * a
     *
     * @return a
     */
    public Pagination clear() {
        this.page = null;
        this.size = null;
        return this;
    }

}
