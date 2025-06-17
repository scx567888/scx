package cool.scx.websocket.x;

import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1ServerResponse;
import cool.scx.websocket.ScxServerWebSocketHandshakeResponse;
import cool.scx.websocket.ScxWebSocket;

import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_ACCEPT;
import static cool.scx.http.status.HttpStatus.SWITCHING_PROTOCOLS;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;
import static cool.scx.websocket.WebSocketHelper.generateSecWebSocketAccept;

public class Http1ServerWebSocketHandshakeResponse implements ScxServerWebSocketHandshakeResponse {

    private final WebSocketOptions webSocketOptions;
    private final Http1ServerWebSocketHandshakeRequest request;
    private final Http1ServerConnection connection;
    private final Http1ServerResponse _response;
    private WebSocket webSocket;

    public Http1ServerWebSocketHandshakeResponse(Http1ServerConnection connection, Http1ServerWebSocketHandshakeRequest request, Http1ServerResponse _response, WebSocketOptions webSocketOptions) {
        this.connection = connection;
        this.request = request;
        this._response = _response;
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public ScxWebSocket webSocket() {
        return webSocket != null ? webSocket : acceptHandshake();
    }

    @Override
    public Http1ServerWebSocketHandshakeRequest request() {
        return this.request;
    }

    @Override
    public ScxHttpStatus status() {
        return _response.status();
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return _response.headers();
    }

    @Override
    public ScxHttpServerResponse status(ScxHttpStatus code) {
        return _response.status(code);
    }

    @Override
    public ScxHttpServerResponse headers(ScxHttpHeaders headers) {
        return _response.headers(headers);
    }

    @Override
    public Void send(MediaWriter writer) {
        return _response.send(writer);
    }

    @Override
    public boolean isSent() {
        return _response.isSent();
    }

    private ScxWebSocket acceptHandshake() {
        // 实现握手接受逻辑, 返回适当的响应头
        if (webSocket == null) {
            _response.headers().upgrade(WEB_SOCKET);
            _response.headers().connection(UPGRADE);
            _response.headers().set(SEC_WEBSOCKET_ACCEPT, generateSecWebSocketAccept(request().secWebSocketKey()));
            status(SWITCHING_PROTOCOLS).send();

            webSocket = new WebSocket(connection.tcpSocket, connection.dataReader, connection.dataWriter, webSocketOptions, false);
            // 一旦成功接受了 websocket 请求, 整个 tcp 将会被 websocket 独占 所以这里需要停止 http 监听
            connection.stop();
        }
        return webSocket;
    }

}
