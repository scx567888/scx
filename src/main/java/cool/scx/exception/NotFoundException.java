package cool.scx.exception;

import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * 404 not found 未找到异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class NotFoundException extends ScxHttpException {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        VoHelper.fillTextPlainContentType(ctx.request().response().setStatusCode(404))
                .end("Not Found !!!");
    }

}
