package cool.scx.data.query.exception;

import cool.scx.data.query.WhereType;

/**
 * 当 WhereType 为 IN 或 NOT_IN 时, 有效的参数条目 (指去除 null 后的) 为空 异常
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ValidParamListIsEmptyException extends IllegalArgumentException {

    public ValidParamListIsEmptyException(String name, WhereType whereType) {
        super("Where 参数错误 : name : " + name + " , whereType 类型 : " + whereType + " 中要求, 有效的参数条目(指去除 null 后的) 不能为空 !!!");
    }

}
