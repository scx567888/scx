package cool.scx.data;

import cool.scx.data.query.*;

/**
 * 查询参数类<br>
 * 针对  GroupBy , OrderBy , Limit , Where 等进行的简单封装 <br>
 * 同时附带一些简单的参数校验 <br>
 * 只是 为了方便传递参数使用<br>
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class Query {

    /**
     * 排序的字段
     */
    private final OrderBy orderBy;

    /**
     * 自定义分组 SQL 添加
     */
    private final GroupBy groupBy;

    /**
     * 自定义WHERE 添加
     */
    private final Where where;

    /**
     * 分页参数
     */
    private final Limit limit;

    /**
     * 创建 Query 对象
     */
    public Query() {
        this.orderBy = new OrderBy();
        this.groupBy = new GroupBy();
        this.where = new Where();
        this.limit = new Limit();
    }

    /**
     * a
     *
     * @param oldQuery a
     */
    public Query(Query oldQuery) {
        this.orderBy = new OrderBy(oldQuery.orderBy);
        this.groupBy = new GroupBy(oldQuery.groupBy);
        this.where = new Where(oldQuery.where);
        this.limit = new Limit(oldQuery.limit);
    }

    /**
     * <p>orderBy.</p>
     *
     * @return a {@link OrderBy} object
     */
    public OrderBy orderBy() {
        return orderBy;
    }

    /**
     * <p>groupBy.</p>
     *
     * @return a {@link GroupBy} object
     */
    public GroupBy groupBy() {
        return groupBy;
    }

    /**
     * <p>where.</p>
     *
     * @return a {@link Where} object
     */
    public Where where() {
        return where;
    }

    /**
     * <p>pagination.</p>
     *
     * @return a {@link Limit} object
     */
    public Limit limit() {
        return limit;
    }

    /**
     * 添加一个 分组字段
     *
     * @param name    分组字段的名称 (默认是实体类的字段名 , 不是数据库中的字段名)
     * @param options 配置
     * @return 本身, 方便链式调用
     */
    public Query addGroupBy(String name, GroupByOption... options) {
        this.groupBy.add(name, options);
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public Query removeGroupBy(String name) {
        this.groupBy.remove(name);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public Query clearGroupBy() {
        this.groupBy.clear();
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param offset   偏移量
     * @param rowCount 长度
     * @return p
     */
    public Query setLimit(long offset, long rowCount) {
        limit.set(offset, rowCount);
        return this;
    }

    /**
     * 设置分页 默认 第一页
     *
     * @param offset a {@link java.lang.Integer} object.
     * @return a 当前实例
     */
    public Query setLimitOffset(long offset) {
        limit.setOffset(offset);
        return this;
    }

    /**
     * 设置分页 默认 第一页
     *
     * @param rowCount a {@link java.lang.Integer} object.
     * @return a 当前实例
     */
    public Query setLimitRowCount(long rowCount) {
        limit.setRowCount(rowCount);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public Query clearLimitOffset() {
        limit.clearOffset();
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public Query clearLimitRowCount() {
        limit.clearRowCount();
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public Query clearLimit() {
        limit.clear();
        return this;
    }

    /**
     * 添加一个排序字段
     *
     * @param orderByColumn 排序字段的名称 (注意是实体类的字段名 , 不是数据库中的字段名)
     * @param orderByType   排序类型 正序或倒序
     * @param options       配置
     * @return 本身, 方便链式调用
     */
    public Query addOrderBy(String orderByColumn, OrderByType orderByType, OrderByOption... options) {
        this.orderBy.add(orderByColumn, orderByType, options);
        return this;
    }

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    字段名称
     * @param options 配置
     * @return a
     */
    public Query asc(String name, OrderByOption... options) {
        this.orderBy.asc(name, options);
        return this;
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    字段名称
     * @param options 配置
     * @return a
     */
    public Query desc(String name, OrderByOption... options) {
        this.orderBy.desc(name, options);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public Query clearOrderBy() {
        this.orderBy.clear();
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public Query removeOrderBy(String name) {
        this.orderBy.remove(name);
        return this;
    }

    /**
     * 不在其中
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query notIn(String name, Object value, WhereOption... options) {
        this.where.notIn(name, value, options);
        return this;
    }

    /**
     * 在其中
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query in(String name, Object value, WhereOption... options) {
        this.where.in(name, value, options);
        return this;
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query jsonContains(String name, Object value, WhereOption... options) {
        this.where.jsonContains(name, value, options);
        return this;
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   默认会在首尾添加 %
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query notLike(String name, Object value, WhereOption... options) {
        this.where.notLike(name, value, options);
        return this;
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   参数 默认会在首尾添加 %
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query like(String name, Object value, WhereOption... options) {
        this.where.like(name, value, options);
        return this;
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   SQL 表达式
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query notLikeRegex(String name, String value, WhereOption... options) {
        this.where.notLikeRegex(name, value, options);
        return this;
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   SQL 表达式
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query likeRegex(String name, String value, WhereOption... options) {
        this.where.likeRegex(name, value, options);
        return this;
    }

    /**
     * 不处于两者之间
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value1  比较值1
     * @param value2  比较值2
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query notBetween(String name, Object value1, Object value2, WhereOption... options) {
        this.where.notBetween(name, value1, value2, options);
        return this;
    }

    /**
     * 两者之间
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value1  比较值1
     * @param value2  比较值2
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query between(String name, Object value1, Object value2, WhereOption... options) {
        this.where.between(name, value1, value2, options);
        return this;
    }

    /**
     * 小于等于
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query lessThanOrEqual(String name, Object value, WhereOption... options) {
        this.where.lessThanOrEqual(name, value, options);
        return this;
    }

    /**
     * 小于
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query lessThan(String name, Object value, WhereOption... options) {
        this.where.lessThan(name, value, options);
        return this;
    }

    /**
     * 大于等于
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query greaterThanOrEqual(String name, Object value, WhereOption... options) {
        this.where.greaterThanOrEqual(name, value, options);
        return this;
    }

    /**
     * 大于
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query greaterThan(String name, Object value, WhereOption... options) {
        this.where.greaterThan(name, value, options);
        return this;
    }

    /**
     * 不相等
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query notEqual(String name, Object value, WhereOption... options) {
        this.where.notEqual(name, value, options);
        return this;
    }

    /**
     * 相等
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param value   比较值
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query equal(String name, Object value, WhereOption... options) {
        this.where.equal(name, value, options);
        return this;
    }

    /**
     * 不为空
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query isNotNull(String name, WhereOption... options) {
        this.where.isNotNull(name, options);
        return this;
    }

    /**
     * 为空
     *
     * @param name    字段名称 (注意 : 不是数据库名称)
     * @param options 配置
     * @return this 方便链式调用
     */
    public Query isNull(String name, WhereOption... options) {
        this.where.isNull(name, options);
        return this;
    }

    /**
     * 获取 whereSQL
     *
     * @return this 方便链式调用
     */
    public Object[] whereSQL() {
        return this.where.whereSQL();
    }

    /**
     * 设置 whereSQL 适用于 复杂查询的自定义 where 子句<br>
     * 支持三种类型 String , WhereBody 和 AbstractPlaceholderSQL
     * 在最终 sql 中会拼接到 where 子句的最后<br>
     * 注意 :  除特殊语法外不需要手动在头部添加 AND
     *
     * @param whereSQL sql 语句
     * @return 本身 , 方便链式调用
     */
    public Query whereSQL(Object... whereSQL) {
        this.where.whereSQL(whereSQL);
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public Query removeWhere(String name) {
        this.where.remove(name);
        return this;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Query clearWhere() {
        this.where.clear();
        return this;
    }

    /**
     * 清楚 where 条件中的 whereSQL
     *
     * @return this 方便链式调用
     */
    public Query clearWhereSQL() {
        this.where.clearWhereSQL();
        return this;
    }

    /**
     * 清除所有 where 条件 (包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Query clearWhereAll() {
        this.where.clearAll();
        return this;
    }

}
