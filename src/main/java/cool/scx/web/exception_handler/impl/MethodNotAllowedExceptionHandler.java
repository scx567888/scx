package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.MethodNotAllowedException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

public class MethodNotAllowedExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new MethodNotAllowedException(info);
    }

}
