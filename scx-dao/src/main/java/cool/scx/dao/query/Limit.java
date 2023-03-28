package cool.scx.dao.query;

/**
 * 条数限制
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Limit {

    /**
     * 当前页 页码 默认为空 即不设置页码
     */
    private Integer offset;

    /**
     * 每页数量分页 每页数量 默认为空 即不设置分页内容
     */
    private Integer rowCount;

    /**
     * 创建一个 Pagination 对象
     */
    public Limit() {
        this.offset = null;
        this.rowCount = null;
    }

    /**
     * 根据旧的 Pagination 创建一个 Pagination 对象
     *
     * @param oldPagination 旧的 Pagination
     */
    public Limit(Limit oldPagination) {
        this.offset = oldPagination.offset;
        this.rowCount = oldPagination.rowCount;
    }

    /**
     * 设置分页参数
     *
     * @param offset   偏移量
     * @param rowCount 长度
     * @return p
     */
    public Limit set(Integer offset, Integer rowCount) {
        if (offset == null || offset < 0) {
            throw new IllegalArgumentException("分页参数错误 : currentPage (分页页码) 不能为空或小于 0 !!!");
        }
        if (rowCount == null || rowCount < 0) {
            throw new IllegalArgumentException("分页参数错误 : pageSize (每页数量) 不能为空或小于 0 !!!");
        }
        this.offset = offset;
        this.rowCount = rowCount;
        return this;
    }

    /**
     * 设置每页数量
     *
     * @param rowCount 长度
     * @return s
     */
    public Limit set(Integer rowCount) {
        return set(0, rowCount);
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    public Integer offset() {
        return offset;
    }

    /**
     * 获取分页大小 (这里分页大小即等于行数 所以直接返回 pageSize)
     *
     * @return rowCount
     */
    public Integer rowCount() {
        return rowCount;
    }

    /**
     * a
     *
     * @return a
     */
    public Limit clear() {
        this.offset = null;
        this.rowCount = null;
        return this;
    }

}
