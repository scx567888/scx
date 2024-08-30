package cool.scx.data.query;

/**
 * WhereType
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum WhereType {

    /**
     * 等于
     */
    EQUAL,

    /**
     * 不等于
     */
    NOT_EQUAL,

    /**
     * 小于
     */
    LESS_THAN,

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL,

    /**
     * 大于
     */
    GREATER_THAN,

    /**
     * 大于等于
     */
    GREATER_THAN_OR_EQUAL,

    /**
     * 为空
     */
    IS_NULL,

    /**
     * 不为空
     */
    IS_NOT_NULL,

    /**
     * Like
     */
    LIKE,

    /**
     * Not Like
     */
    NOT_LIKE,

    /**
     * Like 正则表达式
     */
    LIKE_REGEX,

    /**
     * Not Like 正则表达式
     */
    NOT_LIKE_REGEX,

    /**
     * IN
     */
    IN,

    /**
     * NOT IN
     */
    NOT_IN,

    /**
     * 在之间
     */
    BETWEEN,

    /**
     * 不在之间
     */
    NOT_BETWEEN,

    /**
     * json 包含 一般用于 数组判断
     */
    JSON_CONTAINS

}
