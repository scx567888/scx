package cool.scx.data.query;

import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;
import static cool.scx.data.query.WhereType.*;

/// QueryBuilder
///
/// @author scx567888
/// @version 0.0.1
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

    public static Junction and(Object... clauses) {
        return new And().add(clauses);
    }

    public static Junction or(Object... clauses) {
        return new Or().add(clauses);
    }

    public static Not not(Object clause) {
        return new Not(clause);
    }

    /// 正序 : 也就是从小到大 (1,2,3,4,5,6)
    public static OrderBy asc(String name, QueryOption... options) {
        return new OrderBy(name, ASC, options);
    }

    /// 倒序 : 也就是从大到小 (6,5,4,3,2,1)
    public static OrderBy desc(String name, QueryOption... options) {
        return new OrderBy(name, DESC, options);
    }

    /// 相等 (支持 null 比较)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where eq(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, EQ, value, null, options);
    }

    /// 不相等 (支持 null 比较)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where ne(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NE, value, null, options);
    }

    /// 小于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where lt(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LT, value, null, options);
    }

    /// 小于等于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where lte(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LTE, value, null, options);
    }

    /// 大于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where gt(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, GT, value, null, options);
    }

    /// 大于等于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where gte(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, GTE, value, null, options);
    }

    /// 双端模糊匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     参数 默认会在首尾添加 %
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where like(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, LIKE, value, null, options);
    }

    /// NOT 双端模糊匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     默认会在首尾添加 %
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where notLike(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NOT_LIKE, value, null, options);
    }

    /// 正则表达式匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     SQL 表达式
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where likeRegex(String fieldName, String value, QueryOption... options) {
        return new Where(fieldName, LIKE_REGEX, value, null, options);
    }

    /// NOT 正则表达式匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     SQL 表达式
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where notLikeRegex(String fieldName, String value, QueryOption... options) {
        return new Where(fieldName, NOT_LIKE_REGEX, value, null, options);
    }

    /// 在集合内 (集合元素中 null 也是合法匹配项, 空集合则表示不匹配任何项)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where in(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, IN, value, null, options);
    }

    /// 不在集合内 (集合元素中 null 也是合法匹配项, 空集合则表示不匹配任何项)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where notIn(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, NOT_IN, value, null, options);
    }

    /// 在范围内 (low <= field <= high)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value1    比较值1
    /// @param value2    比较值2
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where between(String fieldName, Object value1, Object value2, QueryOption... options) {
        return new Where(fieldName, BETWEEN, value1, value2, options);
    }

    /// 不在范围内 (field < low 或 field > high)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value1    比较值1
    /// @param value2    比较值2
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where notBetween(String fieldName, Object value1, Object value2, QueryOption... options) {
        return new Where(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    /// JSON 包含某子结构, 针对 JSON 对象或数组的子集匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where jsonContains(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, JSON_CONTAINS, value, null, options);
    }

    /// JSON 数组之间有交集
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param options   配置
    /// @return this 方便链式调用
    public static Where jsonOverlaps(String fieldName, Object value, QueryOption... options) {
        return new Where(fieldName, JSON_OVERLAPS, value, null, options);
    }

    public static WhereClause whereClause(String whereClause, Object... params) {
        return new WhereClause(whereClause, params);
    }

}
