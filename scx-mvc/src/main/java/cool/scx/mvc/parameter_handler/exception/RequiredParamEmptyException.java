package cool.scx.mvc.parameter_handler.exception;

/**
 * 必须参数缺失异常
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class RequiredParamEmptyException extends Exception {

    /**
     * a
     *
     * @param message a
     */
    public RequiredParamEmptyException(String message) {
        super(message);
    }

}
