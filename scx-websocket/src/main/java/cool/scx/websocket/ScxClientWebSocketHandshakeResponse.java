package cool.scx.websocket;

import cool.scx.http.ScxHttpClientResponse;

/// ScxClientWebSocketHandshakeResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxClientWebSocketHandshakeResponse extends ScxHttpClientResponse {

    boolean handshakeSucceeded();

    ScxWebSocket webSocket();

}
