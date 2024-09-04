package cool.scx.data.query;

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

    public static Query where(Object... whereClauses) {
        return new QueryImpl().where(whereClauses);
    }

    public static Query groupBy(Object... groupByClauses) {
        return new QueryImpl().groupBy(groupByClauses);
    }

    public static Query orderBy(Object... orderByClauses) {
        return new QueryImpl().orderBy(orderByClauses);
    }

    public static Query offset(long limitOffset) {
        return new QueryImpl().offset(limitOffset);
    }

    public static Query limit(long numberOfRows) {
        return new QueryImpl().limit(numberOfRows);
    }

    public static Logic and(Object... clauses) {
        return new Logic(LogicType.AND).add(clauses);
    }

    public static Logic or(Object... clauses) {
        return new Logic(LogicType.OR).add(clauses);
    }

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderBy asc(String name, QueryOption... options) {
        return new OrderBy(name, ASC, options);
    }

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     *
     * @param name    a
     * @param options 配置
     * @return a
     */
    public static OrderBy desc(String name, QueryOption... options) {
        return new OrderBy(name, DESC, options);
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where eq(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, EQUAL, value, null, options);
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where ne(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NOT_EQUAL, value, null, options);
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where lt(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LESS_THAN, value, null, options);
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where le(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LESS_THAN_OR_EQUAL, value, null, options);
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where gt(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, GREATER_THAN, value, null, options);
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where ge(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, GREATER_THAN_OR_EQUAL, value, null, options);
    }


    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where isNull(String fieldName, QueryOption... options) {
        return new Where(fieldName, IS_NULL, null, null, options);
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where isNotNull(String fieldName, QueryOption... options) {
        return new Where(fieldName, IS_NOT_NULL, null, null, options);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where like(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LIKE, value, null, options);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where notLike(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NOT_LIKE, value, null, options);
    }


    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where likeRegex(String fieldName, String value, QueryOption... options) {
        return new Where(fieldName, LIKE_REGEX, value, null, options);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where notLikeRegex(String fieldName, String value, QueryOption... options) {
        return new Where(fieldName, NOT_LIKE_REGEX, value, null, options);
    }


    /**
     * 在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where in(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, IN, value, null, options);
    }

    /**
     * 不在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where notIn(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NOT_IN, value, null, options);
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
    public static Where between(String fieldName, Object value1, Object value2, QueryOption... options) {
        return new Where(fieldName, BETWEEN, value1, value2, options);
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
    public static Where notBetween(String fieldName, Object value1, Object value2, QueryOption... options) {
        return new Where(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static Where jsonContains(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, JSON_CONTAINS, value, null, options);
    }

    public static Where jsonOverlaps(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, JSON_OVERLAPS, value, null, options);
    }

    public static WhereClause whereClause(String whereClause, Object... params) {
        return new WhereClause(whereClause, params);
    }

}
