package cool.scx.mvc.exception_handler;

import cool.scx.mvc.exception.InternalServerErrorException;
import io.vertx.ext.web.RoutingContext;

import java.lang.System.Logger;

import static cool.scx.mvc.ScxMvcHelper.responseCanUse;
import static java.lang.System.Logger.Level.ERROR;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastExceptionHandler extends ScxHttpExceptionHandler {

    /**
     * a
     */
    private static final Logger logger = System.getLogger(LastExceptionHandler.class.getName());

    public LastExceptionHandler(boolean useDevelopmentErrorPage) {
        super(useDevelopmentErrorPage);
    }

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
            logger.log(ERROR, "ScxHttpRouter 发生异常 !!!", throwable);
            this.handleScxHttpException(new InternalServerErrorException(throwable), context);
        } else {
            logger.log(ERROR, "ScxHttpRouter 发生异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
