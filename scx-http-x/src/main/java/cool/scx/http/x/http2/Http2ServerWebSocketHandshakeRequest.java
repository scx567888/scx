package cool.scx.http.x.http2;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;

//todo 未完成
public class Http2ServerWebSocketHandshakeRequest implements ScxServerWebSocketHandshakeRequest {

    @Override
    public ScxServerWebSocketHandshakeResponse response() {
        return null;
    }

    @Override
    public ScxHttpMethod method() {
        return null;
    }

    @Override
    public ScxURI uri() {
        return null;
    }

    @Override
    public HttpVersion version() {
        return null;
    }

    @Override
    public ScxHttpHeaders headers() {
        return null;
    }

    @Override
    public ScxHttpBody body() {
        return null;
    }

    @Override
    public PeerInfo remotePeer() {
        return null;
    }

    @Override
    public PeerInfo localPeer() {
        return null;
    }

}
