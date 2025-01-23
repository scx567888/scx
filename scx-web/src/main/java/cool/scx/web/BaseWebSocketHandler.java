package cool.scx.web;

import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.web.annotation.ScxWebSocketRoute;

/**
 * BaseWebSocketHandler 基本接口 请与 {@link ScxWebSocketRoute} 配合使用
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface BaseWebSocketHandler {

    /**
     * 连接 打开 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onOpen(ScxServerWebSocket context) throws Exception {
        
    }

}
