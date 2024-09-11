package cool.scx.web.exception_handler;

import cool.scx.http.ScxRoutingContext;
import cool.scx.web.exception.InternalServerErrorException;

import java.lang.System.Logger;

import static cool.scx.web.ScxWebHelper.responseCanUse;
import static java.lang.System.Logger.Level.ERROR;

public final class LastExceptionHandler extends ScxHttpExceptionHandler {

    private static final Logger logger = System.getLogger(LastExceptionHandler.class.getName());

    public LastExceptionHandler(boolean useDevelopmentErrorPage) {
        super(useDevelopmentErrorPage);
    }

    @Override
    public boolean canHandle(Throwable throwable) {
        return true;
    }

    @Override
    public void handle(Throwable throwable, ScxRoutingContext routingContext) {
        //1, 如果这时 response 还没有被关闭的话 就返回 500 错误信息
        if (responseCanUse(routingContext)) {
            //打印错误信息
            logger.log(ERROR, "ScxHttpRouter 发生异常 !!!", throwable);
            this.handleScxHttpException(new InternalServerErrorException(throwable), routingContext);
        } else {
            logger.log(ERROR, "ScxHttpRouter 发生异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
