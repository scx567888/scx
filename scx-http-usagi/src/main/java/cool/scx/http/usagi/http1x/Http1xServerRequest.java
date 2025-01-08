package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;

import static cool.scx.http.usagi.http1x.Http1xHelper.*;

/**
 * Http1xServerRequest
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerRequest implements ScxHttpServerRequest {

    public final Http1xConnection http1xConnection;
    public final boolean isKeepAlive;

    private final ScxHttpMethod method;
    private final ScxURI uri;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;
    private final PeerInfo remotePeer;
    private final PeerInfo localPeer;

    public Http1xServerRequest(Http1xConnection http1xConnection, Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        this.http1xConnection = http1xConnection;
        this.isKeepAlive = checkIsKeepAlive(requestLine, headers);
        this.method = requestLine.method();
        // todo uri 需要 通过请求头 , socket 等 获取 请求主机 
        this.uri = ScxURI.of(requestLine.path());
        this.version = requestLine.version();
        this.headers = headers;
        this.body = body;
        this.response = new Http1xServerResponse(http1xConnection, this);
        this.remotePeer = getRemotePeer(http1xConnection.tcpSocket);
        this.localPeer = getLocalPeer(http1xConnection.tcpSocket);
    }

    @Override
    public ScxHttpMethod method() {
        return method;
    }

    @Override
    public ScxURI uri() {
        return uri;
    }

    @Override
    public HttpVersion version() {
        return version;
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpBody body() {
        return body;
    }

    @Override
    public ScxHttpServerResponse response() {
        return this.response;
    }

    @Override
    public PeerInfo remotePeer() {
        return remotePeer;
    }

    @Override
    public PeerInfo localPeer() {
        return localPeer;
    }

}
