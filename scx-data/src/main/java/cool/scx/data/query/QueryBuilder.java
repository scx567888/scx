package cool.scx.data.query;

import static cool.scx.data.query.BuildControl.checkUseExpression;
import static cool.scx.data.query.BuildControl.checkUseExpressionValue;
import static cool.scx.data.query.ConditionType.*;
import static cool.scx.data.query.OrderByType.ASC;
import static cool.scx.data.query.OrderByType.DESC;
import static cool.scx.data.query.SkipIfInfo.ofSkipIfInfo;

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

    public static Query where(Where where) {
        return new QueryImpl().where(where);
    }

    public static Query orderBys(OrderBy... orderBys) {
        return new QueryImpl().orderBys(orderBys);
    }

    public static Query offset(long offset) {
        return new QueryImpl().offset(offset);
    }

    public static Query limit(long limit) {
        return new QueryImpl().limit(limit);
    }

    public static Junction and(Where... clauses) {
        return new And().add(clauses);
    }

    public static Junction or(Where... clauses) {
        return new Or().add(clauses);
    }

    public static Not not(Where clause) {
        return new Not(clause);
    }

    public static Condition condition(String fieldName, ConditionType conditionType, Object value, BuildControl... controls) {
        var useExpression = checkUseExpression(controls);
        var useExpressionValue = checkUseExpressionValue(controls);
        var skipIfInfo = ofSkipIfInfo(controls);
        return new Condition(fieldName, conditionType, value, null, useExpression, useExpressionValue, skipIfInfo);
    }

    public static Condition condition(String fieldName, ConditionType conditionType, Object value1, Object value2, BuildControl... controls) {
        var useExpression = checkUseExpression(controls);
        var useExpressionValue = checkUseExpressionValue(controls);
        var skipIfInfo = ofSkipIfInfo(controls);
        return new Condition(fieldName, conditionType, value1, value2, useExpression, useExpressionValue, skipIfInfo);
    }

    public static OrderBy orderBy(String selector, OrderByType orderByType, BuildControl... controls) {
        var useExpression = checkUseExpression(controls);
        return new OrderBy(selector, orderByType, useExpression);
    }

    public static WhereClause whereClause(String expression, Object... params) {
        return new WhereClause(expression , params);
    }

    /// 正序 : 也就是从小到大 (1,2,3,4,5,6)
    public static OrderBy asc(String selector, BuildControl... controls) {
        return orderBy(selector, ASC, controls);
    }

    /// 倒序 : 也就是从大到小 (6,5,4,3,2,1)
    public static OrderBy desc(String selector, BuildControl... controls) {
        return orderBy(selector, DESC, controls);
    }

    /// 相等 (支持 null 比较)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition eq(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, EQ, value, controls);
    }

    /// 不相等 (支持 null 比较)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition ne(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, NE, value, controls);
    }

    /// 小于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition lt(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, LT, value, controls);
    }

    /// 小于等于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition lte(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, LTE, value, controls);
    }

    /// 大于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition gt(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, GT, value, controls);
    }

    /// 大于等于
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition gte(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, GTE, value, controls);
    }

    /// 双端模糊匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     参数 默认会在首尾添加 %
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition like(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, LIKE, value, controls);
    }

    /// NOT 双端模糊匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     默认会在首尾添加 %
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition notLike(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, NOT_LIKE, value, controls);
    }

    /// 正则表达式匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     SQL 表达式
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition likeRegex(String fieldName, String value, BuildControl... controls) {
        return condition(fieldName, LIKE_REGEX, value, controls);
    }

    /// NOT 正则表达式匹配
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     SQL 表达式
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition notLikeRegex(String fieldName, String value, BuildControl... controls) {
        return condition(fieldName, NOT_LIKE_REGEX, value, controls);
    }

    /// 在集合内 (集合元素中 null 也是合法匹配项, 空集合则表示不匹配任何项)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition in(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, IN, value, controls);
    }

    /// 不在集合内 (集合元素中 null 也是合法匹配项, 空集合则表示不匹配任何项)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value     比较值
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition notIn(String fieldName, Object value, BuildControl... controls) {
        return condition(fieldName, NOT_IN, value, controls);
    }

    /// 在范围内 (low <= field <= high)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value1    比较值1
    /// @param value2    比较值2
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition between(String fieldName, Object value1, Object value2, BuildControl... controls) {
        return condition(fieldName, BETWEEN, value1, value2, controls);
    }

    /// 不在范围内 (field < low 或 field > high)
    ///
    /// @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
    /// @param value1    比较值1
    /// @param value2    比较值2
    /// @param controls  配置
    /// @return this 方便链式调用
    public static Condition notBetween(String fieldName, Object value1, Object value2, BuildControl... controls) {
        return condition(fieldName, NOT_BETWEEN, value1, value2, controls);
    }

}
