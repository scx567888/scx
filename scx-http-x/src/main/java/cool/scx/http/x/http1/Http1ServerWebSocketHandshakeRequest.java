package cool.scx.http.x.http1;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;

/// 基于 http1 的 websocket 握手请求
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerWebSocketHandshakeRequest implements ScxServerWebSocketHandshakeRequest {

    public final Http1ServerRequest request;
    private final Http1ServerWebSocketHandshakeResponse response;

    public Http1ServerWebSocketHandshakeRequest(Http1ServerConnection connection, Http1RequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        this.request = new Http1ServerRequest(connection, requestLine, headers, body);
        this.response = new Http1ServerWebSocketHandshakeResponse(connection, this);
    }

    @Override
    public ScxServerWebSocketHandshakeResponse response() {
        return this.response;
    }

    @Override
    public ScxHttpMethod method() {
        return request.method();
    }

    @Override
    public ScxURI uri() {
        return request.uri();
    }

    @Override
    public HttpVersion version() {
        return request.version();
    }

    @Override
    public ScxHttpHeaders headers() {
        return request.headers();
    }

    @Override
    public ScxHttpBody body() {
        return request.body();
    }

    @Override
    public PeerInfo remotePeer() {
        return request.remotePeer();
    }

    @Override
    public PeerInfo localPeer() {
        return request.localPeer();
    }

}
