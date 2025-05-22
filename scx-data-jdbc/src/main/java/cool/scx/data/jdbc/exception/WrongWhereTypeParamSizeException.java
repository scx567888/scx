package cool.scx.data.jdbc.exception;

import cool.scx.data.query.WhereType;

/// WhereBody 参数长度异常
///
/// @author scx567888
/// @version 0.0.1
public final class WrongWhereTypeParamSizeException extends IllegalArgumentException {

    public WrongWhereTypeParamSizeException(String name, WhereType whereType, int paramSize) {
        super("Where 参数错误 : name : " + name + " , whereType 类型 : " + whereType + " , 有效 (不为 null) 的参数数量必须为 " + paramSize + " 个 !!!");
    }

}
