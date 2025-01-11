package cool.scx.http.usagi.http1x;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.usagi.web_socket.ServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

/**
 * 基于 http1 的 websocket 握手请求
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerWebSocketHandshakeRequest extends Http1xServerRequest implements ScxServerWebSocketHandshakeRequest {

    public ServerWebSocket webSocket;

    public Http1xServerWebSocketHandshakeRequest(Http1xServerConnection connection, Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        super(connection, requestLine, headers, body);
    }

    @Override
    public ServerWebSocket acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        if (webSocket == null) {
            var response = response();
            response.setHeader(UPGRADE, "websocket");
            response.setHeader(CONNECTION, "Upgrade");
            response.setHeader(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(secWebSocketKey()));
            response.status(101).send();
            webSocket = new ServerWebSocket(this);
            // 一旦成功接受了 websocket 请求, 整个 tcp 将会被 websocket 独占 所以这里需要停止 http 监听
            connection.stop();
        }
        return webSocket;
    }

    @Override
    public ServerWebSocket webSocket() {
        return webSocket != null ? webSocket : acceptHandshake();
    }

}
