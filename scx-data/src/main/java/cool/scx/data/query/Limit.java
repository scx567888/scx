package cool.scx.data.query;

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
    private Long offset;

    /**
     * 每页数量分页 每页数量 默认为空 即不设置分页内容
     */
    private Long rowCount;

    /**
     * 创建一个 Limit 对象
     */
    public Limit() {
        this.offset = null;
        this.rowCount = null;
    }

    /**
     * 根据旧的 Limit 创建一个 Limit 对象
     *
     * @param oldLimit 旧的 Limit
     */
    public Limit(Limit oldLimit) {
        this.offset = oldLimit.offset;
        this.rowCount = oldLimit.rowCount;
    }

    /**
     * Limit
     *
     * @param offset   偏移量
     * @param rowCount 长度
     */
    public Limit(long offset, long rowCount) {
        set(offset, rowCount);
    }

    /**
     * setOffset
     *
     * @param offset offset (偏移量)
     * @return self
     */
    public Limit setOffset(long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : offset (偏移量) 不能小于 0 !!!");
        }
        this.offset = offset;
        return this;
    }

    /**
     * setRowCount
     *
     * @param rowCount rowCount (行长度)
     * @return self
     */
    public Limit setRowCount(long rowCount) {
        if (rowCount < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : rowCount (行长度) 不能小于 0 !!!");
        }
        this.rowCount = rowCount;
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param offset   偏移量
     * @param rowCount 长度
     * @return self
     */
    public Limit set(long offset, long rowCount) {
        setOffset(offset);
        setRowCount(rowCount);
        return this;
    }

    /**
     * clearOffset
     *
     * @return self
     */
    public Limit clearOffset() {
        this.offset = null;
        return this;
    }

    /**
     * clearRowCount
     *
     * @return self
     */
    public Limit clearRowCount() {
        this.rowCount = null;
        return this;
    }

    /**
     * clear
     *
     * @return self
     */
    public Limit clear() {
        clearOffset();
        clearRowCount();
        return this;
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    public Long offset() {
        return offset;
    }

    /**
     * 获取分页大小 (这里分页大小即等于行数 所以直接返回 pageSize)
     *
     * @return rowCount
     */
    public Long rowCount() {
        return rowCount;
    }

}
