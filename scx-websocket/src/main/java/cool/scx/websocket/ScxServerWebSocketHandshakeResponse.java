package cool.scx.websocket;

import cool.scx.http.ScxHttpServerResponse;

/// ScxServerWebSocketHandshakeResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    @Override
    ScxServerWebSocketHandshakeRequest request();

    ScxWebSocket webSocket();

}
