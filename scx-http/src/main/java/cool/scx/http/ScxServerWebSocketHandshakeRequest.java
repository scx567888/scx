package cool.scx.http;

import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

public interface ScxServerWebSocketHandshakeRequest extends ScxHttpServerRequest {

    default String secWebSocketKey() {
        return getHeader("Sec-Websocket-Key");
    }

    default String secWebSocketVersion() {
        return getHeader("Sec-WebSocket-Version");
    }

    default void acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        var response = response();
        response.setHeader("Upgrade", "websocket");
        response.setHeader("Connection", "Upgrade");
        response.setHeader("Sec-WebSocket-Accept", generateSecWebSocketAccept(secWebSocketKey()));
        response.status(101).send();
    }

    ScxServerWebSocket webSocket();

}
