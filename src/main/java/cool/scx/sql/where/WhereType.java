package cool.scx.sql.where;

import cool.scx.util.tuple.Tuple2;

import static cool.scx.sql.where.WhereTypeHandler.*;

/**
 * WhereType <br>
 * where 查询条件的一些类型 如果无法满足要求请使用 whereSQL
 *
 * @author scx567888
 * @version 1.2.0
 */
public enum WhereType {

    /**
     * 为空
     */
    IS_NULL(0, "IS NULL", IS_NULL_HANDLER),

    /**
     * 不为空
     */
    IS_NOT_NULL(0, "IS NOT NULL", IS_NOT_NULL_HANDLER),

    /**
     * 等于
     */
    EQUAL(1, "=", EQUAL_HANDLER),

    /**
     * 不等于
     */
    NOT_EQUAL(1, "<>", NOT_EQUAL_HANDLER),

    /**
     * 小于
     */
    LESS_THAN(1, "<", LESS_THAN_HANDLER),

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL(1, "<=", LESS_THAN_OR_EQUAL_HANDLER),

    /**
     * 大于
     */
    GREATER_THAN(1, ">", GREATER_THAN_HANDLER),

    /**
     * 大于等于
     */
    GREATER_THAN_OR_EQUAL(1, ">=", GREATER_THAN_OR_EQUAL_HANDLER),

    /**
     * Like
     */
    LIKE(1, "LIKE", LIKE_HANDLER),

    /**
     * Not Like
     */
    NOT_LIKE(1, "NOT LIKE", NOT_LIKE_HANDLER),

    /**
     * Like 正则表达式
     */
    LIKE_REGEX(1, "LIKE", LIKE_REGEX_HANDLER),

    /**
     * Like 正则表达式
     */
    NOT_LIKE_REGEX(1, "NOT LIKE", NOT_LIKE_REGEX_HANDLER),

    /**
     * IN
     */
    IN(1, "IN", IN_HANDLER),

    /**
     * NOT IN
     */
    NOT_IN(1, "NOT IN", NOT_IN_HANDLER),

    /**
     * 在之间
     */
    BETWEEN(2, "BETWEEN", BETWEEN_HANDLER),

    /**
     * 不在之间
     */
    NOT_BETWEEN(2, "NOT BETWEEN", NOT_BETWEEN_HANDLER),

    /**
     * json 包含 一般用于 数组判断
     */
    JSON_CONTAINS(1, "JSON_CONTAINS", JSON_CONTAINS_HANDLER);

    /**
     * 参数数量 用于校验
     */
    private final int paramSize;

    /**
     * 关键词
     */
    private final String keyWord;

    /**
     * handler
     */
    private final WhereTypeHandler whereTypeHandler;

    /**
     * 设置 参数数量 和关键字
     *
     * @param paramSize        p
     * @param keyWord          k
     * @param whereTypeHandler a
     */
    WhereType(int paramSize, String keyWord, WhereTypeHandler whereTypeHandler) {
        this.paramSize = paramSize;
        this.keyWord = keyWord;
        this.whereTypeHandler = whereTypeHandler;
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

    /**
     * 获取关键词
     *
     * @return key
     */
    public String keyWord() {
        return keyWord;
    }

    Tuple2<Object[], String> getWhereParamsAndWhereClause(String name, Object value1, Object value2, WhereOption.Info info) {
        return whereTypeHandler.getWhereParamsAndWhereClause(name, this, value1, value2, info);
    }

}
