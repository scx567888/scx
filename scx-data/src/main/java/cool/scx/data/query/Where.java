package cool.scx.data.query;

import cool.scx.data.query.WhereOption.Info;

import static cool.scx.data.query.WhereType.*;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Where {

    /**
     * 自定义的查询语句
     */
    private Object[] whereBodyList;

    /**
     * 创建一个 Where 对象
     */
    public Where() {
        this.whereBodyList = new Object[]{};
    }

    /**
     * 根据旧的 Where 创建一个 Where 对象
     *
     * @param oldWhere 旧的 Where
     */
    public Where(Where oldWhere) {
        this.whereBodyList = oldWhere.whereBodyList();
    }

    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NULL, null, null, new Info(options));
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNotNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NOT_NULL, null, null, new Info(options));
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody equal(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, EQUAL, value, null, new Info(options));
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notEqual(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, NOT_EQUAL, value, null, new Info(options));
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody greaterThan(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, GREATER_THAN, value, null, new Info(options));
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody greaterThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, GREATER_THAN_OR_EQUAL, value, null, new Info(options));
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody lessThan(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, LESS_THAN, value, null, new Info(options));
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody lessThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return new WhereBody(fieldName, LESS_THAN_OR_EQUAL, value, null, new Info(options));
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
        return new WhereBody(fieldName, BETWEEN, value1, value2, new Info(options));
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
        return new WhereBody(fieldName, NOT_BETWEEN, value1, value2, new Info(options));
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
        return new WhereBody(fieldName, LIKE_REGEX, value, null, new Info(options));
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
        return new WhereBody(fieldName, NOT_LIKE_REGEX, value, null, new Info(options));
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
        return new WhereBody(fieldName, LIKE, value, null, new Info(options));
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
        return new WhereBody(fieldName, NOT_LIKE, value, null, new Info(options));
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
        return new WhereBody(fieldName, JSON_CONTAINS, value, null, new Info(options));
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
        return new WhereBody(fieldName, IN, value, null, new Info(options));
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
        return new WhereBody(fieldName, NOT_IN, value, null, new Info(options));
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return whereBodyList.length == 0;
    }

    /**
     * 设置 whereSQL 适用于 复杂查询的自定义 where 子句<br>
     * 支持三种类型 String , WhereBody 和 AbstractPlaceholderSQL
     * 在最终 cool.scx.sql 中会拼接到 where 子句的最后<br>
     * 注意 :  除特殊语法外不需要手动在头部添加 AND
     *
     * @param whereClauses cool.scx.sql 语句
     * @return 本身 , 方便链式调用
     */
    public Where set(Object... whereClauses) {
        this.whereBodyList = whereClauses;
        return this;
    }

    public Object[] whereBodyList() {
        return whereBodyList;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Where clear() {
        whereBodyList = new Object[]{};
        return this;
    }

}
