package cool.scx.http.exception_handler.impl;

import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception_handler.ScxHttpRouterExceptionHandler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a
 */
public final class ScxHttpExceptionHandler implements ScxHttpRouterExceptionHandler {

    /**
     * a
     */
    public static final ScxHttpExceptionHandler DEFAULT_INSTANCE = new ScxHttpExceptionHandler();

    /**
     * a
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxHttpExceptionHandler.class);

    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof ScxHttpException;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        if (!context.request().response().ended() && !context.request().response().closed()) {
            ((ScxHttpException) throwable).handle(context);
        } else {
            logger.error("捕获到 ScxHttpException 异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
