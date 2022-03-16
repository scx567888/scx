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
     * a
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 json 查询
     */
    USE_JSON_EXTRACT

}
