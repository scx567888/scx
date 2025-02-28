package cool.scx.web.parameter_handler.exception;

/// 必须参数缺失异常
///
/// @author scx567888
/// @version 0.0.1
public final class RequiredParamEmptyException extends Exception {

    public RequiredParamEmptyException(String message) {
        super(message);
    }

}
