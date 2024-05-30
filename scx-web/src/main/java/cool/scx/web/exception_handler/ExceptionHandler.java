package cool.scx.web.exception_handler;

import io.vertx.ext.web.RoutingContext;

/**
 * 路由异常处理器
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ExceptionHandler {

    /**
     * 判断是否可以处理这个异常
     *
     * @param throwable 异常
     * @return a
     */
    boolean canHandle(Throwable throwable);

    /**
     * 将结果处理并返回
     *
     * @param throwable      a
     * @param routingContext a
     */
    void handle(Throwable throwable, RoutingContext routingContext);

}
