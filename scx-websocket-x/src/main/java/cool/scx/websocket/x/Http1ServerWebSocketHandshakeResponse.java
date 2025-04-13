package cool.scx.websocket.x;

import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1ServerResponse;
import cool.scx.websocket.ScxServerWebSocket;
import cool.scx.websocket.ScxServerWebSocketHandshakeResponse;

import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_ACCEPT;
import static cool.scx.http.status.HttpStatus.SWITCHING_PROTOCOLS;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;

public class Http1ServerWebSocketHandshakeResponse extends Http1ServerResponse implements ScxServerWebSocketHandshakeResponse {

    private final WebSocketOptions webSocketOptions;
    private ServerWebSocket webSocket;

    public Http1ServerWebSocketHandshakeResponse(Http1ServerConnection connection, Http1ServerWebSocketHandshakeRequest request, WebSocketOptions webSocketOptions) {
        super(connection, request);
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public ScxServerWebSocket acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        if (webSocket == null) {
            headers.upgrade(WEB_SOCKET);
            headers.connection(UPGRADE);
            headers.set(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(request().secWebSocketKey()));
            status(SWITCHING_PROTOCOLS).send();

            webSocket = new ServerWebSocket(connection.tcpSocket, connection.dataReader, connection.dataWriter, webSocketOptions, request());
            // 一旦成功接受了 websocket 请求, 整个 tcp 将会被 websocket 独占 所以这里需要停止 http 监听
            connection.stop();
        }
        return webSocket;
    }

    @Override
    public ScxServerWebSocket webSocket() {
        return webSocket != null ? webSocket : acceptHandshake();
    }

    @Override
    public Http1ServerWebSocketHandshakeRequest request() {
        return (Http1ServerWebSocketHandshakeRequest) this.request;
    }

}
