package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.UnauthorizedException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new UnauthorizedException(info);
    }

}
