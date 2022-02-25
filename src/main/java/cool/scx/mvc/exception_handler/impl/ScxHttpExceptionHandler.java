package cool.scx.mvc.exception_handler.impl;

import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception.impl.CustomHttpException;
import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a
 */
public final class ScxHttpExceptionHandler implements ScxMappingExceptionHandler {

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
        if (!(throwable instanceof CustomHttpException)) {
            if (context.request().response().ended()) {
                logger.error("捕获到 ScxHttpException 异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
                return;
            }
        }
        ((ScxHttpException) throwable).handle(context);
    }

}
