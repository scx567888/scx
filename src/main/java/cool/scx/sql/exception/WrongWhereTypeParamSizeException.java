package cool.scx.sql.exception;

import cool.scx.sql.where.WhereType;

/**
 * 错误的 WhereBody 参数长度异常
 *
 * @author scx567888
 * @version 1.14.4
 */
public final class WrongWhereTypeParamSizeException extends IllegalArgumentException {

    /**
     * a
     *
     * @param whereType a
     */
    public WrongWhereTypeParamSizeException(WhereType whereType) {
        super("Where 参数错误 : whereType 类型 : " + whereType + " , 有效 (不为 null) 的参数数量必须为 " + whereType.paramSize());
    }

}
