package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;

public class MethodNotAllowedException extends ScxHttpException {

    public MethodNotAllowedException() {
        super(405, ScxConstant.HTTP_METHOD_NOT_ALLOWED_TITLE, "");
    }

    public MethodNotAllowedException(String info) {
        super(405, ScxConstant.HTTP_METHOD_NOT_ALLOWED_TITLE, info);
    }

}
