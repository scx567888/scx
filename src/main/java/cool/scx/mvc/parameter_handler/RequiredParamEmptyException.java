package cool.scx.mvc.parameter_handler;

/**
 * 必须参数缺失异常
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