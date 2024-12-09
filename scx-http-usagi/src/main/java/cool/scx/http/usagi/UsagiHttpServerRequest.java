package cool.scx.http.usagi;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.tcp.ScxTCPSocket;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
class UsagiHttpServerRequest implements ScxHttpServerRequest {

    final ScxTCPSocket tcpSocket;
    ScxHttpMethod method;
    ScxURI uri;
    HttpVersion version;
    ScxHttpHeaders headers;
    ScxHttpBody body;
    ScxHttpServerResponse response;
    PeerInfo remotePeer;
    PeerInfo localPeer;

    public UsagiHttpServerRequest(ScxTCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
        //todo 这里现在是假的
        remotePeer = PeerInfo.of();
        localPeer = PeerInfo.of();
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
