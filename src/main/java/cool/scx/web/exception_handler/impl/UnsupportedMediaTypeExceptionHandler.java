package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.UnsupportedMediaTypeException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

/**
 * 415 参数异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class UnsupportedMediaTypeExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new UnsupportedMediaTypeException(info());
    }

}
