package cool.scx.http.exception_handler.impl;

import cool.scx.http.exception.impl.InternalServerErrorException;
import cool.scx.http.exception_handler.ScxHttpRouterExceptionHandler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a
 */
public final class LastExceptionHandler implements ScxHttpRouterExceptionHandler {

    /**
     * a
     */
    public static final LastExceptionHandler DEFAULT_INSTANCE = new LastExceptionHandler();

    /**
     * a
     */
    private static final Logger logger = LoggerFactory.getLogger(LastExceptionHandler.class);

    @Override
    public boolean canHandle(Throwable throwable) {
        return true;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        //1, 如果这时 response 还没有被关闭的话 就返回 500 错误信息
        if (!context.response().ended() && !context.response().closed()) {
            //打印错误信息
            logger.error("ScxHttpRouter 发生异常 !!!", throwable);
            ScxHttpExceptionHandler.handleScxHttpException(new InternalServerErrorException(throwable), context);
        } else {
            logger.error("ScxHttpRouter 发生异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
