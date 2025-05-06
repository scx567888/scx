package cool.scx.websocket.x;

import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1ServerRequest;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;

import java.io.InputStream;

/// 基于 http1 的 websocket 握手请求
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerWebSocketHandshakeRequest implements ScxServerWebSocketHandshakeRequest {

    private final WebSocketOptions webSocketOptions;//应该最先注入
    private final Http1ServerRequest _request;
    private final Http1ServerWebSocketHandshakeResponse response;

    public Http1ServerWebSocketHandshakeRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, InputStream bodyInputStream, WebSocketOptions webSocketOptions) {
        this._request = new Http1ServerRequest(connection, requestLine, headers, bodyInputStream);
        this.response = new Http1ServerWebSocketHandshakeResponse(connection, this, this._request.response(), webSocketOptions);
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public Http1ServerWebSocketHandshakeResponse response() {
        return this.response;
    }

    @Override
    public ScxHttpMethod method() {
        return _request.method();
    }

    @Override
    public ScxURI uri() {
        return _request.uri();
    }

    @Override
    public HttpVersion version() {
        return _request.version();
    }

    @Override
    public ScxHttpHeaders headers() {
        return _request.headers();
    }

    @Override
    public ScxHttpBody body() {
        return _request.body();
    }

    @Override
    public PeerInfo remotePeer() {
        return _request.remotePeer();
    }

    @Override
    public PeerInfo localPeer() {
        return _request.localPeer();
    }

}
