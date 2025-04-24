package cool.scx.websocket;

import cool.scx.http.ScxHttpClientResponse;

public interface ScxClientWebSocketHandshakeResponse extends ScxHttpClientResponse {

    boolean handshakeSucceeded();

    ScxWebSocket webSocket();

}
