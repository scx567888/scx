package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.headers.connection.ConnectionType;
import cool.scx.http.x.web_socket.ServerWebSocket;

import java.io.OutputStream;

import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_ACCEPT;
import static cool.scx.http.headers.HttpFieldName.UPGRADE;

public class Http1ServerWebSocketHandshakeResponse implements ScxServerWebSocketHandshakeResponse {

    private final Http1ServerConnection connection;
    private final Http1ServerWebSocketHandshakeRequest request;
    private final Http1ServerResponse response;
    private ServerWebSocket webSocket;

    public Http1ServerWebSocketHandshakeResponse(Http1ServerConnection connection, Http1ServerWebSocketHandshakeRequest request) {
        this.connection = connection;
        this.request = request;
        this.response = request.request.response();// 获取到最原始的 响应对象
    }

    @Override
    public ScxServerWebSocket acceptHandshake() {
        // 实现握手接受逻辑，返回适当的响应头
        if (webSocket == null) {
            setHeader(UPGRADE, "websocket");
            headers().connection(ConnectionType.UPGRADE);
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

    @Override
    public ScxServerWebSocketHandshakeRequest request() {
        return request;
    }

    @Override
    public ScxHttpStatus status() {
        return response.status();
    }

    @Override
    public Http1Headers headers() {
        return response.headers();
    }

    @Override
    public ScxHttpServerResponse status(ScxHttpStatus code) {
        return response.status(code);
    }

    @Override
    public OutputStream outputStream() {
        return response.outputStream();
    }

    @Override
    public boolean isSent() {
        return response.isSent();
    }


}
