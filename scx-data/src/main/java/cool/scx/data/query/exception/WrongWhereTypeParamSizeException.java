package cool.scx.data.query.exception;

import cool.scx.data.query.WhereType;

/**
 * 错误的 WhereBody 参数长度异常
 *
 * @author scx567888
 * @version 0.0.1
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
