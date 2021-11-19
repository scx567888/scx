package cool.scx.exception;

import io.vertx.ext.web.RoutingContext;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermException extends ScxHttpException {

    @Override
    public void handle(RoutingContext ctx) {
        ctx.request().response().setStatusCode(403).end("No Perm !!!");
    }

}
