package cool.scx.websocket;

import cool.scx.http.ScxHttpServerResponse;

public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    @Override
    ScxServerWebSocketHandshakeRequest request();

    ScxWebSocket webSocket();

}
