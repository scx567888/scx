package cool.scx.web;

import cool.scx.web.annotation.ScxWebSocketRoute;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;

/// BaseWebSocketHandler 基本接口 请与 [ScxWebSocketRoute] 配合使用
///
/// @author scx567888
/// @version 0.0.1
public interface BaseWebSocketHandler {

    /// 连接 打开 时
    ///
    /// @param request 上下文
    /// @throws java.lang.Exception e
    default void onHandshakeRequest(ScxServerWebSocketHandshakeRequest request) throws Exception {

    }

}
