package cool.scx.websocket;

import cool.scx.http.ScxHttpServerResponse;

import java.util.function.Consumer;

public interface ScxServerWebSocketHandshakeResponse extends ScxHttpServerResponse {

    @Override
    ScxServerWebSocketHandshakeRequest request();

    ScxWebSocket acceptHandshake();

    ScxWebSocket webSocket();

    default void onWebSocket(Consumer<ScxEventWebSocket> consumer) {
        var ws = webSocket();
        var eventWS = ScxEventWebSocket.of(ws);
        consumer.accept(eventWS);
        eventWS.start();
    }

}
