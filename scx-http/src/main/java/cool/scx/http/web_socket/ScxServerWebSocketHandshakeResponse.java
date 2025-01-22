package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerResponse;

public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    @Override
    ScxServerWebSocketHandshakeRequest request();

    ScxServerWebSocket acceptHandshake();

    ScxServerWebSocket webSocket();

}
