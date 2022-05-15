package cool.scx.sql.exception;

import cool.scx.sql.where.WhereType;

/**
 * 当 whereType 为 in 或 not in 时 有效的参数条目 (指去除 null 后的) 为空
 */
public final class ValidParamListIsEmptyException extends IllegalArgumentException {

    /**
     * a
     *
     * @param whereType a
     */
    public ValidParamListIsEmptyException(WhereType whereType) {
        super("Where 参数错误 : whereType 类型 : " + whereType + " 中要求, 有效的参数条目(指去除 null 后的) 不能为空 !!!");
    }

}
