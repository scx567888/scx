package cool.scx.exception.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.exception.ScxHttpExceptionHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * 未认证异常 (未登录)
 *
 * @author scx567888
 * @version 1.1.19
 */
public class UnauthorizedException extends ScxHttpException {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        ScxHttpExceptionHelper.sendException(401, "Unauthorized !!!", "", ctx);
    }

}
