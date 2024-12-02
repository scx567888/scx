package cool.scx.data.query;

/**
 * WhereType
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum WhereType {

    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    IS_NULL,
    IS_NOT_NULL,
    LIKE,
    NOT_LIKE,
    LIKE_REGEX,
    NOT_LIKE_REGEX,
    IN,
    NOT_IN,
    BETWEEN,
    NOT_BETWEEN,
    JSON_CONTAINS,
    JSON_OVERLAPS

}
