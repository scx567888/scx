package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerResponse;

import java.util.function.Consumer;

public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    @Override
    ScxServerWebSocketHandshakeRequest request();

    ScxServerWebSocket acceptHandshake();

    ScxServerWebSocket webSocket();

    default void onWebSocket(Consumer<ScxServerWebSocket> consumer) {
        var ws = webSocket();
        consumer.accept(ws);
        ws.start();
    }

}
