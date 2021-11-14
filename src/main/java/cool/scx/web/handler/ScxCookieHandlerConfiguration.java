package cool.scx.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * <p>ScxCookieHandlerConfiguration class.</p>
 *
 * @author scx567888
 * @version 1.3.14
 */
public final class ScxCookieHandlerConfiguration {

    /**
     * 默认直接走向下一个路由
     */
    private static Handler<RoutingContext> scxCookieHandler = RoutingContext::next;

    /**
     * 获取 cookieHandler
     *
     * @return c
     */
    public static Handler<RoutingContext> getScxCookieHandler() {
        return scxCookieHandler;
    }

    /**
     * 注意别忘了在 方法中执行 RoutingContext.next
     *
     * @param cookieHandler 消费者
     */
    public static void setScxCookieHandler(Handler<RoutingContext> cookieHandler) {
        scxCookieHandler = cookieHandler;
    }

}
