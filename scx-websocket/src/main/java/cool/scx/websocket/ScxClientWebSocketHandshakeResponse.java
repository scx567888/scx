package cool.scx.websocket;

import cool.scx.http.ScxHttpClientResponse;

import java.util.function.Consumer;

public interface ScxClientWebSocketHandshakeResponse extends ScxHttpClientResponse {

    boolean handshakeSucceeded();

    ScxClientWebSocket webSocket();

    default void onWebSocket(Consumer<ScxClientWebSocket> consumer) {
        var ws = webSocket();
        consumer.accept(ws);
        ws.start();
    }

}
