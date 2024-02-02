package cool.scx.data.query;

/**
 * WhereType <br>
 * where 查询条件的一些类型 如果无法满足要求请使用 whereSQL
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum WhereType {

    /**
     * 为空
     */
    IS_NULL(0),

    /**
     * 不为空
     */
    IS_NOT_NULL(0),

    /**
     * 等于
     */
    EQUAL(1),

    /**
     * 不等于
     */
    NOT_EQUAL(1),

    /**
     * 小于
     */
    LESS_THAN(1),

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL(1),

    /**
     * 大于
     */
    GREATER_THAN(1),

    /**
     * 大于等于
     */
    GREATER_THAN_OR_EQUAL(1),

    /**
     * Like
     */
    LIKE(1),

    /**
     * Not Like
     */
    NOT_LIKE(1),

    /**
     * Like 正则表达式
     */
    LIKE_REGEX(1),

    /**
     * Like 正则表达式
     */
    NOT_LIKE_REGEX(1),

    /**
     * IN
     */
    IN(1),

    /**
     * NOT IN
     */
    NOT_IN(1),

    /**
     * 在之间
     */
    BETWEEN(2),

    /**
     * 不在之间
     */
    NOT_BETWEEN(2),

    /**
     * json 包含 一般用于 数组判断
     */
    JSON_CONTAINS(1);

    /**
     * 参数数量 用于校验
     */
    private final int paramSize;

    /**
     * 设置 参数数量
     *
     * @param paramSize p
     */
    WhereType(int paramSize) {
        this.paramSize = paramSize;
    }

    /**
     * a
     *
     * @param whereTypeStr a
     * @return a
     */
    public static WhereType of(String whereTypeStr) {
        return WhereType.valueOf(whereTypeStr.trim().toUpperCase());
    }

    /**
     * 获取参数数量
     *
     * @return 参数数量
     */
    public int paramSize() {
        return paramSize;
    }

}
