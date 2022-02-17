package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;
import cool.scx.exception.ScxHttpExceptionHelper;
import cool.scx.util.ExceptionUtils;
import io.vertx.ext.web.RoutingContext;

/**
 * 请求错误异常
 *
 * @author scx567888
 * @version 1.1.15
 */
public class BadRequestException extends ScxHttpException {

    /**
     * 错误信息
     */
    private final String throwableMessage;

    /**
     * <p>Constructor for BadRequestException.</p>
     */
    public BadRequestException() {
        this.throwableMessage = "Bad Request !!!";
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param throwableMessage a {@link java.lang.Throwable} object
     */
    public BadRequestException(String throwableMessage) {
        this.throwableMessage = throwableMessage;
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     */
    public BadRequestException(Throwable throwable) {
        this.throwableMessage = ExceptionUtils.getCustomStackTrace(throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext ctx) {
        ScxHttpExceptionHelper.sendException(400, ScxConstant.HTTP_BAD_REQUEST_TITLE, throwableMessage, ctx);
    }

}
