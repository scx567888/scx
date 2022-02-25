package cool.scx.http.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.http.exception.ScxHttpException;

/**
 * 方法不被允许
 */
public class MethodNotAllowedException extends ScxHttpException {

    public MethodNotAllowedException() {
        super(405, ScxConstant.HTTP_METHOD_NOT_ALLOWED_TITLE);
    }

    public MethodNotAllowedException(String info) {
        super(405, ScxConstant.HTTP_METHOD_NOT_ALLOWED_TITLE, info);
    }

    public MethodNotAllowedException(Throwable throwable) {
        super(405, ScxConstant.HTTP_METHOD_NOT_ALLOWED_TITLE, throwable);
    }

}
