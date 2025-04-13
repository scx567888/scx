package cool.scx.websocket.x;

import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1ServerRequest;
import cool.scx.http.x.http1.Http1ServerResponse;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;

import java.io.InputStream;

/// 基于 http1 的 websocket 握手请求
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerWebSocketHandshakeRequest extends Http1ServerRequest implements ScxServerWebSocketHandshakeRequest {

    private final WebSocketOptions webSocketOptions;

    public Http1ServerWebSocketHandshakeRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, InputStream bodyInputStream, WebSocketOptions webSocketOptions) {
        super(connection, requestLine, headers, bodyInputStream);
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public Http1ServerWebSocketHandshakeResponse response() {
        return (Http1ServerWebSocketHandshakeResponse) this.response;
    }

    @Override
    protected Http1ServerResponse createResponse() {
        return new Http1ServerWebSocketHandshakeResponse(connection, this, webSocketOptions);
    }

}
