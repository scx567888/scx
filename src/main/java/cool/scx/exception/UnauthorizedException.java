package cool.scx.exception;

import cool.scx.util.VoHelper;
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
        VoHelper.fillTextPlainContentType(ctx.request().response().setStatusCode(401))
                .end("Unauthorized !!!");
    }

}
