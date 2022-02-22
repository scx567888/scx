package cool.scx.web.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.impl.NoPermException;
import cool.scx.web.exception_handler.ScxRouterExceptionHandler;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermExceptionHandler extends ScxRouterExceptionHandler {

    @Override
    public ScxHttpException getScxHttpException(String info) {
        return new NoPermException(info);
    }

}
