package cool.scx.data;

import cool.scx.data.query.*;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;
import static cool.scx.data.query.WhereType.*;

public final class QueryBuilder {

    public static Query query() {
        return new QueryImpl();
    }

    public static Query query(Query oldQuery) {
        return new QueryImpl(oldQuery);
    }

    public static Where where(Object... whereClauses) {
        return new Where().set(whereClauses);
    }

    public static GroupBy groupBy(Object... groupByClauses) {
        return new GroupBy().set(groupByClauses);
    }

    public static OrderBy orderBy(Object... orderByClauses) {
        return new OrderBy().set(orderByClauses);
    }

    public static LimitInfo offset(long limitOffset) {
        return new LimitInfo().offset(limitOffset);
    }

    public static LimitInfo limit(long numberOfRows) {
        return new LimitInfo().limit(numberOfRows);
    }

    public static AND and(Object... clauses) {
        return new AND(clauses);
    }

    public static OR or(Object... clauses) {
        return new OR(clauses);
    }

    public static WhereBodySet andSet() {
        return new WhereBodySet(LogicType.AND);
    }

    public static WhereBodySet orSet() {
        return new WhereBodySet(LogicType.OR);
    }

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderByBody asc(String name, OrderByOption... options) {
        return new OrderByBody(name, ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderByBody desc(String name, OrderByOption... options) {
        return new OrderByBody(name, DESC, options);
    }

    public static OrderByBodySet orderBySet() {
        return new OrderByBodySet();
    }

    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NULL, null, null, options);
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNotNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NOT_NULL, null, null, options);
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody eq(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, EQUAL, value, null, options);
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody ne(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, NOT_EQUAL, value, null, options);
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody gt(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, GREATER_THAN, value, null, options);
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody ge(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, GREATER_THAN_OR_EQUAL, value, null, options);
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody lt(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, LESS_THAN, value, null, options);
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody le(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, LESS_THAN_OR_EQUAL, value, null, options);
    }

    /**
     * 两者之间
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody between(String fieldName, Object value1, Object value2, WhereOption... options) {
        return new WhereBody(fieldName, BETWEEN, value1, value2, options);
    }

    /**
     * 不处于两者之间
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notBetween(String fieldName, Object value1, Object value2, WhereOption... options) {
        return new WhereBody(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody likeRegex(String fieldName, String value, WhereOption... options) {
        return new WhereBody(fieldName, LIKE_REGEX, value, null, options);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notLikeRegex(String fieldName, String value, WhereOption... options) {
        return new WhereBody(fieldName, NOT_LIKE_REGEX, value, null, options);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody like(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, LIKE, value, null, options);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notLike(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, NOT_LIKE, value, null, options);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody jsonContains(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, JSON_CONTAINS, value, null, options);
    }

    /**
     * 在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody in(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, IN, value, null, options);
    }

    /**
     * 不在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notIn(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, NOT_IN, value, null, options);
    }

    public static WhereClause whereClause(String whereClause, Object... params) {
        return new WhereClause(whereClause, params);
    }

}
