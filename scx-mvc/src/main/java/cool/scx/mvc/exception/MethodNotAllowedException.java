package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

/**
 * 方法不被允许
 *
 * @author scx567888
 * @version 1.11.8
 */
public class MethodNotAllowedException extends ScxHttpException {

    /**
     * a
     */
    public MethodNotAllowedException() {
        super(ScxHttpResponseStatus.METHOD_NOT_ALLOWED.statusCode(), ScxHttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public MethodNotAllowedException(String info) {
        super(ScxHttpResponseStatus.METHOD_NOT_ALLOWED.statusCode(), ScxHttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public MethodNotAllowedException(Throwable throwable) {
        super(ScxHttpResponseStatus.METHOD_NOT_ALLOWED.statusCode(), ScxHttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public MethodNotAllowedException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.METHOD_NOT_ALLOWED.statusCode(), ScxHttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase(), info, throwable);
    }

}
