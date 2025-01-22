package cool.scx.http.x.http1x;

import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;
import cool.scx.http.x.web_socket.ServerWebSocket;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

public class Http1xServerWebSocketHandshakeResponse extends AbstractHttp1xServerResponse<Http1xServerWebSocketHandshakeRequest> implements ScxServerWebSocketHandshakeResponse {

    private ServerWebSocket webSocket;

    public Http1xServerWebSocketHandshakeResponse(Http1xServerConnection connection, Http1xServerWebSocketHandshakeRequest request) {
        super(connection, request);
    }

    @Override
    public ScxServerWebSocket acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        if (webSocket == null) {
            setHeader(UPGRADE, "websocket");
            setHeader(CONNECTION, "Upgrade");
            setHeader(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(request.secWebSocketKey()));
            status(101).send();

            webSocket = new ServerWebSocket(connection.tcpSocket, connection.dataReader, connection.dataWriter, connection.options.webSocketOptions(), request);
            // 一旦成功接受了 websocket 请求, 整个 tcp 将会被 websocket 独占 所以这里需要停止 http 监听
            connection.stop();
        }
        return webSocket;
    }

    @Override
    public ScxServerWebSocket webSocket() {
        return webSocket != null ? webSocket : acceptHandshake();
    }

}
