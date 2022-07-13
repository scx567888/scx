package cool.scx.core.http.exception_handler;

import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public interface ScxHttpRouterExceptionHandler {

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
     * @param throwable a
     * @param context   a
     */
    void handle(Throwable throwable, RoutingContext context);

}
