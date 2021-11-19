package cool.scx.exception;

import io.vertx.ext.web.RoutingContext;

/**
 * 415 参数异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class UnsupportedMediaTypeException extends ScxHttpException {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        ctx.request().response().setStatusCode(415).end("Unsupported Media Type !!!");
    }

}
