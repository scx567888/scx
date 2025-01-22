package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpClientResponse;

public interface ScxClientWebSocketHandshakeResponse extends ScxHttpClientResponse {

    boolean handshakeSucceeded();

    ScxClientWebSocket webSocket();

}
