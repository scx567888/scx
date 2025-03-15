package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerRequest;

import java.util.function.Consumer;

import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_KEY;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_VERSION;

/// ScxServerWebSocketHandshakeRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxServerWebSocketHandshakeRequest extends ScxHttpServerRequest {

    @Override
    ScxServerWebSocketHandshakeResponse response();

    default String secWebSocketKey() {
        return getHeader(SEC_WEBSOCKET_KEY);
    }

    default String secWebSocketVersion() {
        return getHeader(SEC_WEBSOCKET_VERSION);
    }

    default ScxServerWebSocket acceptHandshake() {
        return response().acceptHandshake();
    }

    default ScxServerWebSocket webSocket() {
        return response().webSocket();
    }

    default void onWebSocket(Consumer<ScxServerWebSocket> consumer) {
        response().onWebSocket(consumer);
    }

}
