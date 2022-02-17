package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;
import cool.scx.exception.ScxHttpExceptionHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * 404 not found 未找到异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class NotFoundException extends ScxHttpException {

    /**
     * a
     */
    private final String message;

    /**
     * a
     */
    public NotFoundException() {
        this.message = "";
    }

    /**
     * a
     *
     * @param message a
     */
    public NotFoundException(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        ScxHttpExceptionHelper.sendException(404, ScxConstant.HTTP_NOT_FOUND_TITLE, message, ctx);
    }

}
