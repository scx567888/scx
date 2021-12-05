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
    private Integer page = null;

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
     * <p>Constructor for Pagination.</p>
     *
     * @param page a int
     * @param size a int
     */
    public Pagination(int page, int size) {
        set(page, size);
    }

    /**
     * 默认取第一页
     *
     * @param size a int
     */
    public Pagination(int size) {
        set(size);
    }

    /**
     * 设置分页参数
     *
     * @param page 分页页码
     * @param size a int
     * @return p
     */
    public Pagination set(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("分页参数错误 : page 不能小于 1 !!!");
        } else if (size < 0) {
            throw new IllegalArgumentException("分页参数错误 : size 不能小于 0 !!!");
        } else {
            this.page = page;
            this.size = size;
            return this;
        }
    }

    /**
     * <p>set.</p>
     *
     * @param size s
     * @return s
     */
    public Pagination set(int size) {
        return set(1, size);
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    public Integer offset() {
        if (page != null && size != null) {
            return (page - 1) * size;
        } else {
            return null;
        }
    }

    /**
     * 获取分页大小
     *
     * @return a int
     */
    public Integer size() {
        return size;
    }

}
