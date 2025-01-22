package cool.scx.http.x.http1x;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;
import cool.scx.http.x.web_socket.ServerWebSocket;

import java.io.OutputStream;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

public class Http1xServerWebSocketHandshakeResponse implements ScxServerWebSocketHandshakeResponse {

    private final Http1xServerConnection connection;
    private final Http1xServerWebSocketHandshakeRequest request;
    private final ScxHttpServerResponse response;
    private ServerWebSocket webSocket;

    public Http1xServerWebSocketHandshakeResponse(Http1xServerConnection connection, Http1xServerWebSocketHandshakeRequest request) {
        this.connection = connection;
        this.request = request;
        this.response = request.request.response();// 获取到最原始的 响应对象
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

    @Override
    public ScxServerWebSocketHandshakeRequest request() {
        return request;
    }

    @Override
    public HttpStatusCode status() {
        return response.status();
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return response.headers();
    }

    @Override
    public ScxHttpServerResponse status(HttpStatusCode code) {
        return response.status(code);
    }

    @Override
    public OutputStream outputStream() {
        return response.outputStream();
    }

    @Override
    public void end() {
        response.end();
    }

    @Override
    public boolean isClosed() {
        return response.isClosed();
    }


}
