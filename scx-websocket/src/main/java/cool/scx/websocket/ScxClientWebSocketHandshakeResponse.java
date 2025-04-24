package cool.scx.websocket;

import cool.scx.http.ScxHttpClientResponse;

import java.util.function.Consumer;

public interface ScxClientWebSocketHandshakeResponse extends ScxHttpClientResponse {

    boolean handshakeSucceeded();

    ScxWebSocket webSocket();

    default void onWebSocket(Consumer<ScxEventWebSocket> consumer) {
        var ws = webSocket();
        var eventWS = ScxEventWebSocket.of(ws);
        consumer.accept(eventWS);
        eventWS.start();
    }

}
