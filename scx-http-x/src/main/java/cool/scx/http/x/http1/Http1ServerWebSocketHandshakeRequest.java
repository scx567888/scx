package cool.scx.http.x.http1;

import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;

import java.io.InputStream;

/// 基于 http1 的 websocket 握手请求
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerWebSocketHandshakeRequest extends Http1ServerRequest implements ScxServerWebSocketHandshakeRequest {

    public Http1ServerWebSocketHandshakeRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, InputStream bodyInputStream) {
        super(connection, requestLine, headers, bodyInputStream);
    }

    @Override
    public Http1ServerWebSocketHandshakeResponse response() {
        return (Http1ServerWebSocketHandshakeResponse) this.response;
    }

    @Override
    public Http1ServerResponse createResponse() {
        return new Http1ServerWebSocketHandshakeResponse(connection, this);
    }

}
