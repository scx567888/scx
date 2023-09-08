package cool.scx.data.query;

import cool.scx.data.Query;

/**
 * 条数限制
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class LimitInfo extends QueryLike<LimitInfo> {

    /**
     * 当前页 页码 默认为空 即不设置页码
     */
    private Long offset;

    /**
     * 每页数量分页 每页数量 默认为空 即不设置分页内容
     */
    private Long limit;

    /**
     * 创建一个 Limit 对象
     */
    public LimitInfo() {
        this.offset = null;
        this.limit = null;
    }

    /**
     * 根据旧的 Limit 创建一个 Limit 对象
     *
     * @param oldLimit 旧的 Limit
     */
    public LimitInfo(LimitInfo oldLimit) {
        this.offset = oldLimit.offset;
        this.limit = oldLimit.limit;
    }

    /**
     * setOffset
     *
     * @param limitOffset offset (偏移量)
     * @return self
     */
    @Override
    public LimitInfo offset(long limitOffset) {
        if (limitOffset < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : offset (偏移量) 不能小于 0 !!!");
        }
        this.offset = limitOffset;
        return this;
    }

    /**
     * setLimit
     *
     * @param numberOfRows limit (行长度)
     * @return self
     */
    @Override
    public LimitInfo limit(long numberOfRows) {
        if (numberOfRows < 0) {
            throw new IllegalArgumentException("Limit 参数错误 : limit (行长度) 不能小于 0 !!!");
        }
        this.limit = numberOfRows;
        return this;
    }

    /**
     * 获取偏移量
     *
     * @return a int
     */
    @Override
    public Long getOffset() {
        return offset;
    }

    /**
     * 获取分页大小 (这里分页大小即等于行数 所以直接返回 pageSize)
     *
     * @return limit
     */
    @Override
    public Long getLimit() {
        return limit;
    }

    /**
     * clearOffset
     *
     * @return self
     */
    @Override
    public LimitInfo clearOffset() {
        this.offset = null;
        return this;
    }

    /**
     * clearLimit
     *
     * @return self
     */
    @Override
    public LimitInfo clearLimit() {
        this.limit = null;
        return this;
    }

    @Override
    public LimitInfo getLimitInfo() {
        return this;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl(this);
    }

}
