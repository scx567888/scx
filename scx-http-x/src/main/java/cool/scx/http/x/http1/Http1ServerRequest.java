package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;

import java.io.InputStream;

import static cool.scx.http.x.http1.Http1Helper.*;
import static cool.scx.http.x.http1.headers.connection.Connection.CLOSE;

/// HTTP/1.1 ServerRequest
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerRequest implements ScxHttpServerRequest {

    protected final Http1ServerConnection connection;
    protected final ScxHttpMethod method;
    protected final ScxURI uri;
    protected final HttpVersion version;
    protected final Http1Headers headers;
    protected final ScxHttpBody body;
    protected final PeerInfo remotePeer;
    protected final PeerInfo localPeer;
    protected final Http1ServerResponse response;

    public Http1ServerRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, InputStream bodyInputStream) {
        this.connection = connection;
        this.method = requestLine.method();
        this.uri = inferURI(requestLine.path(),headers,connection.tcpSocket);
        this.version = requestLine.version();
        this.headers = headers;
        this.body = new Http1Body(bodyInputStream, this.headers);
        this.remotePeer = getRemotePeer(connection.tcpSocket);
        this.localPeer = getLocalPeer(connection.tcpSocket);
        this.response = new Http1ServerResponse(connection, this);
    }

    @Override
    public Http1ServerResponse response() {
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
    public Http1Headers headers() {
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

    public boolean isKeepAlive() {
        return headers.connection() != CLOSE;
    }

}
