package cool.scx.websocket.routing;

import cool.scx.http.routing.TypeMatcher;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;

/// ScxHttp 路由匹配器 WebSocket 实现
///
/// @author scx567888
/// @version 0.0.1
public class WebSocketTypeMatcher {

    public static final TypeMatcher NOT_WEB_SOCKET_HANDSHAKE = (request) -> !(request instanceof ScxServerWebSocketHandshakeRequest);

    public static final TypeMatcher WEB_SOCKET_HANDSHAKE = (request) -> request instanceof ScxServerWebSocketHandshakeRequest;

}
