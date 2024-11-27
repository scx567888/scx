package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpServerRequest;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

public interface ScxServerWebSocketHandshakeRequest extends ScxHttpServerRequest {

    default String secWebSocketKey() {
        return getHeader(SEC_WEBSOCKET_KEY);
    }

    default String secWebSocketVersion() {
        return getHeader(SEC_WEBSOCKET_VERSION);
    }

    default void acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        var response = response();
        response.setHeader(UPGRADE, "websocket");
        response.setHeader(CONNECTION, "Upgrade");
        response.setHeader(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(secWebSocketKey()));
        response.status(101).send();
    }

    ScxServerWebSocket webSocket();

}
