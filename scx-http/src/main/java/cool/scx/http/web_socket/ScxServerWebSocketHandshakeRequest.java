package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerRequest;

import static cool.scx.http.HttpFieldName.SEC_WEBSOCKET_KEY;
import static cool.scx.http.HttpFieldName.SEC_WEBSOCKET_VERSION;

/**
 * ScxServerWebSocketHandshakeRequest
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxServerWebSocketHandshakeRequest extends ScxHttpServerRequest {

    default String secWebSocketKey() {
        return getHeader(SEC_WEBSOCKET_KEY);
    }

    default String secWebSocketVersion() {
        return getHeader(SEC_WEBSOCKET_VERSION);
    }

    ScxServerWebSocket acceptHandshake();

    ScxServerWebSocket webSocket();

}
