package cool.scx.websocket.routing;

import cool.scx.http.routing.TypeMatcher;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;

public class WebSocketTypeMatcher {

    public static final TypeMatcher NOT_WEB_SOCKET_HANDSHAKE = (request) -> !(request instanceof ScxServerWebSocketHandshakeRequest);
    
    public static final TypeMatcher WEB_SOCKET_HANDSHAKE = (request) -> request instanceof ScxServerWebSocketHandshakeRequest;

}
