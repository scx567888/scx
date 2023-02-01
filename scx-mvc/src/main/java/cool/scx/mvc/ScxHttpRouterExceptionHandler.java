package cool.scx.mvc;

import io.vertx.ext.web.RoutingContext;

/**
 * 路由异常处理器
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
