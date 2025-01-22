package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerResponse;

public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    ScxServerWebSocket acceptHandshake();

    ScxServerWebSocket webSocket();

}
