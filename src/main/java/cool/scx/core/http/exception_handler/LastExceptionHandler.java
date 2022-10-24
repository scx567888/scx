package cool.scx.core.http.exception_handler;

import cool.scx.core.http.ScxHttpRouterExceptionHandler;
import cool.scx.core.http.exception.InternalServerErrorException;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cool.scx.core.ScxHelper.responseCanUse;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Throwable throwable) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        //1, 如果这时 response 还没有被关闭的话 就返回 500 错误信息
        if (responseCanUse(context)) {
            //打印错误信息
            logger.error("ScxHttpRouter 发生异常 !!!", throwable);
            ScxHttpExceptionHandler.handleScxHttpException(new InternalServerErrorException(throwable), context);
        } else {
            logger.error("ScxHttpRouter 发生异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
