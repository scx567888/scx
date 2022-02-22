package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.BadRequestException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

/**
 * 请求错误异常
 *
 * @author scx567888
 * @version 1.1.15
 */
public class BadRequestExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new BadRequestException(info());
    }

}
