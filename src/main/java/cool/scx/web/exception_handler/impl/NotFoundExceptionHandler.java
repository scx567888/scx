package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.NotFoundException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;


/**
 * 404 not found 未找到异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class NotFoundExceptionHandler extends ScxRouterExceptionHandler {


    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new NotFoundException(info);
    }

}
