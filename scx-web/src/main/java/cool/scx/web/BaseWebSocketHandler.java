package cool.scx.web;

import cool.scx.http.routing.WebSocketRoutingContext;
import cool.scx.web.annotation.ScxWebSocketRoute;

/**
 * BaseWebSocketHandler 基本接口 请与 {@link ScxWebSocketRoute} 配合使用
 *
 * @author scx567888
 * @version 1.0.10
 */
public interface BaseWebSocketHandler {

    /**
     * 连接 打开 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onOpen(WebSocketRoutingContext context) throws Exception {
        context.next();
    }

}
