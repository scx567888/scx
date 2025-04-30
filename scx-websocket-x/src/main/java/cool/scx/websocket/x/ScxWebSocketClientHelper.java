package cool.scx.websocket.x;

import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxWebSocketClient;

/// ScxHttpClientHelper
///
/// @author scx567888
/// @version 0.0.1
public final class ScxWebSocketClientHelper {

    public static final ScxWebSocketClient DEFAULT_WEBSOCKET_CLIENT = new WebSocketClient();

    public static ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return DEFAULT_WEBSOCKET_CLIENT.webSocketHandshakeRequest();
    }

}
