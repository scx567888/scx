package cool.scx.core.http.exception.impl;

import cool.scx.core.http.exception.ScxHttpException;

import static cool.scx.core.http.ScxHttpResponseStatus.METHOD_NOT_ALLOWED;

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
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public MethodNotAllowedException(String info) {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public MethodNotAllowedException(Throwable throwable) {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public MethodNotAllowedException(String info, Throwable throwable) {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase(), info, throwable);
    }

}
