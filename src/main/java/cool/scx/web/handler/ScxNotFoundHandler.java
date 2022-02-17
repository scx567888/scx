package cool.scx.web.handler;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpExceptionHelper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;


public final class ScxNotFoundHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        ScxHttpExceptionHelper.sendException(404, ScxConstant.HTTP_NOT_FOUND_TITLE, "", routingContext);
    }

}