package cool.scx.http.x.http2;

import cool.scx.http.*;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;

//todo 未完成
public class Http2ServerRequest implements ScxHttpServerRequest {

    @Override
    public ScxHttpServerResponse response() {
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
