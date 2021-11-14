package cool.scx.exception;

import cool.scx.vo.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * 请求错误异常
 *
 * @author scx567888
 * @version 1.1.15
 */
public class BadRequestException extends HttpRequestException {

    /**
     *
     */
    private final Object throwableMessage;

    /**
     * <p>Constructor for BadRequestException.</p>
     */
    public BadRequestException() {
        throwableMessage = null;
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param throwableMessage a {@link java.lang.Throwable} object
     */
    public BadRequestException(Object throwableMessage) {
        this.throwableMessage = throwableMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        if (throwableMessage == null) {
            ctx.response().setStatusCode(400).end("Bad Request !!!");
        } else {
            ctx.response().setStatusCode(400);
            Json.fail("Request Parameter Wrong !!!").put("message", throwableMessage).handle(ctx);
        }
    }

}
