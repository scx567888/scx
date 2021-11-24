package cool.scx.mvc.exception_handler.impl;

import cool.scx.exception.ScxHttpException;
import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import io.vertx.ext.web.RoutingContext;

public final class ScxHttpExceptionHandler implements ScxMappingExceptionHandler {

    public static final ScxHttpExceptionHandler DEFAULT_INSTANCE = new ScxHttpExceptionHandler();

    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof ScxHttpException;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        ((ScxHttpException) throwable).handle(context);
    }

}
