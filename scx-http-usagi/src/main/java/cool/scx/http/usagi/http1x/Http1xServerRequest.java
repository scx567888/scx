package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.LinkedDataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerRequest implements ScxHttpServerRequest {

    public final ScxTCPSocket tcpSocket;
    public final LinkedDataReader dataReader;
    public final OutputStream dataWriter;

    private final ScxHttpMethod method;
    private final ScxURI uri;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;
    private final PeerInfo remotePeer;
    private final PeerInfo localPeer;

    public Http1xServerRequest(Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body, ScxTCPSocket tcpSocket, LinkedDataReader dataReader, OutputStream dataWriter) {
        this.tcpSocket = tcpSocket;
        this.dataReader = dataReader;
        this.dataWriter = dataWriter;

        this.method = requestLine.method();
        // todo uri 需要 通过请求头 , socket 等 获取 请求主机 
        this.uri = ScxURI.of(requestLine.path());
        this.version = requestLine.version();
        this.headers = headers;
        this.body = body;
        this.response = new Http1xServerResponse(this, tcpSocket, dataReader, dataWriter);
        this.remotePeer = PeerInfo.of();
        this.localPeer = PeerInfo.of();
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
