package cool.scx.data.jdbc.exception;

import cool.scx.data.query.WhereType;

/// WhereBody 参数类型异常
///
/// @author scx567888
/// @version 0.0.1
public final class WrongWhereParamTypeException extends IllegalArgumentException {

    public WrongWhereParamTypeException(String name, WhereType whereType, String paramType) {
        super("Where 参数类型错误 : name : " + name + " , whereType 类型 : " + whereType + " , 参数类型无法转换为 " + paramType + " !!!");
    }

}
