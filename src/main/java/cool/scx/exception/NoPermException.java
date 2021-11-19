package cool.scx.exception;

import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermException extends ScxHttpException {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        VoHelper.fillTextPlainContentType(ctx.request().response().setStatusCode(403))
                .end("No Perm !!!");
    }

}
