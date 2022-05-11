package cool.scx.sql.exception;

import cool.scx.sql.where.WhereType;

/**
 * 错误的 WhereBody 参数长度异常
 */
public final class WrongWhereTypeParamSizeException extends IllegalArgumentException {

    public WrongWhereTypeParamSizeException(WhereType whereType) {
        super("Where 参数错误 : whereType 类型 : " + whereType + " , 有效 (不为 null) 的参数数量必须为 " + whereType.paramSize());
    }

}