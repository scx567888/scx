package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_Info.PeerInfo;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;

import static cool.scx.http.x.http1.Http1Helper.*;

/// HTTP/1.1 ServerRequest
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerRequest implements ScxHttpServerRequest {

    public final Http1ServerConnection connection;
    public final boolean isKeepAlive;

    private final Http1ServerResponse response;
    private final ScxHttpMethod method;
    private final ScxURI uri;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final PeerInfo remotePeer;
    private final PeerInfo localPeer;

    public Http1ServerRequest(Http1ServerConnection connection, Http1RequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        this.connection = connection;
        this.isKeepAlive = checkIsKeepAlive(requestLine, headers);

        this.method = requestLine.method();
        // todo uri 需要 通过请求头 , socket 等 获取 请求主机 
        this.uri = ScxURI.of(requestLine.path());
        this.version = requestLine.version();
        this.headers = headers;
        this.body = body;
        this.remotePeer = getRemotePeer(connection.tcpSocket);
        this.localPeer = getLocalPeer(connection.tcpSocket);
        this.response = new Http1ServerResponse(connection, this);
    }

    @Override
    public ScxHttpServerResponse response() {
        return this.response;
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
    public PeerInfo remotePeer() {
        return remotePeer;
    }

    @Override
    public PeerInfo localPeer() {
        return localPeer;
    }

}
