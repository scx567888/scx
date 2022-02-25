package cool.scx.http.exception.impl;

import cool.scx.http.exception.ScxHttpException;

import static cool.scx.http.ScxHttpResponseStatus.METHOD_NOT_ALLOWED;

/**
 * 方法不被允许
 */
public class MethodNotAllowedException extends ScxHttpException {

    public MethodNotAllowedException() {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase());
    }

    public MethodNotAllowedException(String info) {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase(), info);
    }

    public MethodNotAllowedException(Throwable throwable) {
        super(METHOD_NOT_ALLOWED.statusCode(), METHOD_NOT_ALLOWED.reasonPhrase(), throwable);
    }

}
