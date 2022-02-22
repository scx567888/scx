package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.InternalServerErrorException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

public class InternalServerErrorExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new InternalServerErrorException(info);
    }

}
