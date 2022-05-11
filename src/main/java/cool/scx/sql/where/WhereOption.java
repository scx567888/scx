package cool.scx.sql.where;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public enum WhereOption {

    /**
     * a
     */
    REPLACE,

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     */
    SKIP_IF_NULL,

    /**
     * 如果查询的参数值为 null 则忽略错误(不报错,但是可能会导致不合法的 SQL 语句出现)
     * <br>
     * 优先级大于  优先级大于  {@link WhereOption#SKIP_IF_NULL}
     */
    IGNORE_EXCEPTION_IF_NULL,

    /**
     * 在 in 或 not in 中 如果参数列表为空则跳过添加而不是报错
     */
    SKIP_IF_EMPTY_LIST,

    /**
     * 在 in 或 not in 中 如果参数列表为空则忽略错误而不是报错,但是可能会导致不合法的 SQL 语句出现
     * <br>
     * 优先级大于  {@link WhereOption#SKIP_IF_EMPTY_LIST}
     */
    IGNORE_EXCEPTION_IF_EMPTY_LIST,

    /**
     * 在 not in 中 如果参数列表中有任何元素为 null ,则跳过添加而不是报错
     */
    SKIP_IF_NULL_IN_LIST,

    /**
     * 在 not in 中 如果参数列表中有任何元素为 null ,则忽略错误而不是报错,但是可能会导致不合法的 SQL 语句出现
     * <br>
     * 优先级大于  {@link WhereOption#SKIP_IF_NULL_IN_LIST}
     */
    IGNORE_EXCEPTION_IF_NULL_IN_LIST,

    /**
     * a
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 json 查询
     * <br>
     * 注意和 {@link WhereType#JSON_CONTAINS} 一起使用时无效 因为 {@link WhereType#JSON_CONTAINS} 自己有针对 Json 的特殊实现
     */
    USE_JSON_EXTRACT

}
