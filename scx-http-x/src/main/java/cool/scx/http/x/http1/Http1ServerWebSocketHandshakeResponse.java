package cool.scx.http.x.http1;

import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;
import cool.scx.http.x.web_socket.ServerWebSocket;

import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_ACCEPT;
import static cool.scx.http.status.HttpStatus.SWITCHING_PROTOCOLS;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;

public class Http1ServerWebSocketHandshakeResponse extends Http1ServerResponse implements ScxServerWebSocketHandshakeResponse {

    private ServerWebSocket webSocket;

    public Http1ServerWebSocketHandshakeResponse(Http1ServerConnection connection, Http1ServerWebSocketHandshakeRequest request) {
        super(connection, request);
    }

    @Override
    public ScxServerWebSocket acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        if (webSocket == null) {
            headers.upgrade(WEB_SOCKET);
            headers.connection(UPGRADE);
            headers.set(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(request().secWebSocketKey()));
            status(SWITCHING_PROTOCOLS).send();

            webSocket = new ServerWebSocket(connection.tcpSocket, connection.dataReader, connection.dataWriter, connection.options.webSocketOptions(), request());
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
