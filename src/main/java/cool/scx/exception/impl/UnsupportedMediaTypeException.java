package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;
import cool.scx.exception.ScxHttpExceptionHelper;
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
        ScxHttpExceptionHelper.sendException(415, ScxConstant.HTTP_UNSUPPORTED_MEDIA_TYPE_TITLE, "", ctx);
    }

}
